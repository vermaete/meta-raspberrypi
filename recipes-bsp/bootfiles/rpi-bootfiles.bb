DESCRIPTION = "Closed source binary files to help boot all raspberry pi devices."
LICENSE = "Broadcom-RPi"

LIC_FILES_CHKSUM = "file://LICENCE.broadcom;md5=c403841ff2837657b2ed8e5bb474ac8d"

inherit deploy nopackages

RPIFW_DATE ?= "20250326"
SRCREV = "f49a3960223f20ea0b2e30646f65be67bafe30dc"
SHORTREV = "${@d.getVar("SRCREV", False).__str__()[:7]}"
RPIFW_SRC_URI ?= "https://api.github.com/repos/raspberrypi/firmware/tarball/f49a3960223f20ea0b2e30646f65be67bafe30dc;downloadfilename=raspberrypi-firmware-${SHORTREV}.tar.gz"
RPIFW_S ?= "${WORKDIR}/raspberrypi-firmware-${SHORTREV}"

SRC_URI = "${RPIFW_SRC_URI}"
SRC_URI[sha256sum] = "26926a9c56be907a87fafb5bc8add431ec6b763598de340e7401ce5684d2c55b"

PV = "${RPIFW_DATE}"

INHIBIT_DEFAULT_DEPS = "1"

DEPENDS = "rpi-config rpi-cmdline"

COMPATIBLE_MACHINE = "^rpi$"

S = "${RPIFW_S}/boot"

PR = "r3"

do_deploy() {
    install -d ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}

    for i in ${S}/*.elf ; do
        cp $i ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}
    done
    for i in ${S}/*.dat ; do
        cp $i ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}
    done
    for i in ${S}/*.bin ; do
        cp $i ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}
    done

    # Add stamp in deploy directory
    touch ${DEPLOYDIR}/${BOOTFILES_DIR_NAME}/${PN}-${PV}.stamp
}

do_deploy[depends] += "rpi-config:do_deploy rpi-cmdline:do_deploy"

addtask deploy before do_build after do_install
do_deploy[dirs] += "${DEPLOYDIR}/${BOOTFILES_DIR_NAME}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

