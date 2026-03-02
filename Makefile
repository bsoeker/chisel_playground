# ---- Settings -------------------------------------------------------------
TOP     := NexysTop
MILL    := ./mill
MODULE  := chisel_playground
VIVADO  := vivado -mode batch -nojournal -nolog

SV      := generated/$(TOP).sv
XDC     := constraints/Nexys-A7-100T-Master.xdc
BIT     := vivado_out/$(TOP).bit
SCALA   := $(shell find src/main/scala -name '*.scala')

# ---- Targets --------------------------------------------------------------
.PHONY: all verilog bit program test clean distclean

all: bit

verilog: $(SV)
bit:     $(BIT)

# Regenerate Verilog only when a Scala source or the build file changed
$(SV): $(SCALA) build.mill
	$(MILL) $(MODULE).run

# Rebuild the bitstream only when the Verilog, constraints or script changed
$(BIT): $(SV) $(XDC) scripts/build.tcl
	$(VIVADO) -source scripts/build.tcl

program: $(BIT)
	$(VIVADO) -source scripts/program.tcl

test:
	$(MILL) $(MODULE).test

clean:
	rm -rf generated vivado_out .Xil usage_statistics_webtalk.* clockInfo.txt

distclean: clean
	rm -rf out build
