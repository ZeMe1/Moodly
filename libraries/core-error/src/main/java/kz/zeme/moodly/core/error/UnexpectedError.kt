package kz.zeme.moodly.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class UnexpectedError(
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(
    code = Code,
    underlying = underlying,
    message = message,
) {
    @Parcelize
    data object Code : ErrorCode
}
