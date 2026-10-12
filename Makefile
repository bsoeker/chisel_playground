# ---- Settings -------------------------------------------------------------
TOP     := NexysTop
MILL    := ./mill
VIVADO  := vivado -mode batch -nojournal -nolog
BOARD   := ./fpga/nexys

SV      := build/$(TOP).sv
XDC     := $(BOARD)/constraints/Nexys-A7-100T-Master.xdc
BIT     := $(BOARD)/vivado_out/$(TOP).bit
SCALA   := $(shell find src -name '*.scala')

# ---- Targets --------------------------------------------------------------
.PHONY: all verilog bit program test clean distclean

all: bit

verilog: $(SV)
bit:     $(BIT)

# Regenerate Verilog only when a Scala source or the build file changed
$(SV): $(SCALA) build.mill
	$(MILL) run

# Rebuild the bitstream only when the Verilog, constraints or script changed
$(BIT): $(SV) $(XDC) $(BOARD)/build.tcl
	cd $(BOARD) && $(VIVADO) -source build.tcl

program: $(BIT)
	cd $(BOARD) && $(VIVADO) -source program.tcl

test:
	$(MILL) test

clean:
	rm -rf build $(BOARD)/vivado_out $(BOARD)/.Xil $(BOARD)/clockInfo.txt

distclean: clean
	rm -rf out build
