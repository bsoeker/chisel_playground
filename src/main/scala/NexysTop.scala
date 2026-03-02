import chisel3._

class NexysTop extends RawModule {
  val CLK100MHZ = IO(Input(Clock()))
  val CPU_RESETN = IO(Input(Bool()))
  val LED = IO(Output(Bool()))

  withClockAndReset(CLK100MHZ, !CPU_RESETN) {
    val blinky = Module(new Blinky(clockHz = 100_000_000))
    LED := blinky.io.led
  }
}

object Main extends App {
  Emit(new NexysTop, "NexysTop.sv")
}
