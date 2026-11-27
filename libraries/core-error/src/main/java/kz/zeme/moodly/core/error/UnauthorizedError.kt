package kz.zeme.moodly.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class UnauthorizedError(
    override val message: String? = null,
    override val underlying: Throwable? = null,
) : BaseError(
    code = Code,
    underlying = underlying,
    message = message,
) {
    @Parcelize
    data object Code : ErrorCode
}
