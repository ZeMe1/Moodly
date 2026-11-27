package kz.zeme.moodly.core.observable.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import kz.zeme.moodly.core.domain.Response
import kz.zeme.moodly.core.error.UnexpectedError
import kz.zeme.moodly.core.observable.ViewState

fun <T : Any> LiveData<ViewState<T>>.getLoadingState(
    data: T? = value?.data,
    loaderDurationMs: Long = ViewState.LOADER_DEFAULT_DURATION_MS,
): ViewState.Loading<T> = data
    ?.let { ViewState.Loading.ProgressBar(data = it, durationMs = loaderDurationMs) }
    ?: ViewState.Loading.Skeleton(durationMs = loaderDurationMs)

suspend fun <T : Any> LiveData<ViewState<T>>.getViewState(
    response: Response<T>,
): ViewState<T> = getViewState(
    response = response,
    transform = { it },
)

suspend fun <X : Any, Y : Any> LiveData<ViewState<X>>.getViewState(
    response: Response<Y>,
    transform: suspend (Y) -> X,
): ViewState<X> = value?.getViewState(
    response = response,
    transform = transform,
) ?: when (response) {
    is Response.Failure -> ViewState.Error(error = response.data)
    is Response.Success -> try {
        ViewState.Data(transform(response.data))
    } catch (ex: Exception) {
        ViewState.Error(UnexpectedError(underlying = ex))
    }
}

fun <T : Any, F : Any> LiveData<ViewState<T>>.getErrorState(
    response: Response.Failure<F>,
): ViewState.Error<T> = ViewState.Error(
    error = response.data,
    data = value?.data,
)

fun <X : Any, Y : Any> LiveData<ViewState<X>>.mapState(
    transform: (X) -> Y,
): LiveData<ViewState<Y>> = map { it.mapState(transform) }

fun <T : Any> MutableLiveData<ViewState<T>>.update(block: T.() -> T) {
    val update = (value?.data ?: return).block()
    value = ViewState.Data(update)
}

fun <T : Any> MutableLiveData<ViewState<T>>.postUpdate(block: T.() -> T) {
    val update = (value?.data ?: return).block()
    postValue(ViewState.Data(update))
}

fun <T : Any> MutableLiveData<T>.updateValue(block: T.() -> T) {
    val current = value ?: return
    value = current.block()
}

fun <T : Any> MutableLiveData<T>.postValue(block: T.() -> T) {
    val current = value ?: return
    postValue(current.block())
}

fun <T> LiveData<T>.observeOnce(
    owner: LifecycleOwner,
    observer: Observer<T>,
) {
    observe(
        owner,
        object : Observer<T> {
            override fun onChanged(value: T) {
                observer.onChanged(value)
                removeObserver(this)
            }
        },
    )
}
