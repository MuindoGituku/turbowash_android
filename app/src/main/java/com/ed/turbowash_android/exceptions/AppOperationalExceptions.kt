/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.exceptions

class NetworkException(message: String): Exception(message)
class AuthenticationException(message: String): Exception(message)
class DataIntegrityException(message: String): Exception(message)
class StorageException(message: String): Exception(message)
class PermissionDeniedException(message: String): Exception(message)
class ResourceNotFoundException(message: String): Exception(message)
class QuotaExceededException(message: String): Exception(message)
class InvalidDataException(message: String): Exception(message)
class OperationFailedException(message: String): Exception(message)
class TimeoutException(message: String): Exception(message)