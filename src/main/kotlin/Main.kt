import kotlinx.coroutines.*
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2
import org.jetbrains.kotlinx.multik.ndarray.data.NDArray

fun main() = runBlocking {
//	val fix = MultikLoader.loadSync()

	val scope = CoroutineScope(Dispatchers.IO)
	val a = mk.zeros<Float>(2, 2)
	val b = a.copy()
	val deferredList = (0..10).map {
		scope.async {
			linAlgCall(a, b)
		}
	}

	deferredList.awaitAll()
	Unit
}

fun linAlgCall(a: NDArray<Float, D2>, b: NDArray<Float, D2>) {
	// mk.linalg calls a lazy init of library which is not thread safe or does not capture
	// Exception in thread "main" java.nio.file.FileAlreadyExistsException
	mk.linalg.dot(a, b)
}

object MultikLoader {
	@Synchronized
	fun loadSync() {
		val mkLinAlg = mk.linalg
		println("MK engine init: ${mk.engine}, ${mkLinAlg::class.simpleName}")
	}
}