import circt.stage.ChiselStage
import chisel3.RawModule
import java.nio.file.{Files, Paths}

object Emit {
  def apply(gen: => RawModule, fileName: String): Unit = {
    val sv = ChiselStage.emitSystemVerilog(
      gen,
      firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info")
    )
    val dir = Paths.get("build")
    Files.createDirectories(dir)
    Files.writeString(dir.resolve(fileName), sv.split("// ----- 8< -----").head)
  }
}
