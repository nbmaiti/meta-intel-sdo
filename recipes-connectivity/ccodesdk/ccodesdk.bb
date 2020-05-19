#sdo yocto project 

DESCRIPTION = "C-Code-SDK is an example program"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=fa818a259cbed7ce8bc2a22d35a464fc "

##SRCREV = "7aec58d0152d47a3a5cf287e3ef64eb552bf32da"
##SRC_URI = "git://github.com/secure-device-onboard/client-sdk.git"
##SRC_URI[sha256sum] = "25aaa5ea90c023484f09027ae4e17d14dd5e927992a6d9717d0e954208a57f8f"

SRCREV = "bb0012d2b7459877c6a77f271d31c258b56d96cc"
SRC_URI = "git://github.com/nbmaiti/client-sdk.git"
SRC_URI[sha256sum] = "25aaa5ea90c023484f09027ae4e17d14dd5e927992a6d9717d0e954208a57f8f"

S = "${WORKDIR}/git"

TOOLCHAIN = "POKY-GLIBC"

APP_NAME = "c_code_sdk"

DEPENDS += "openssl"

FILES_${PN} += "/opt \
                /opt/sdo \
                /opt/sdo/linux-client" 



do_configure(){
}

do_compile(){
CUR_DIR=$(pwd)
cd "${WORKDIR}/git"
export CFLAGS="-D_IPP_v50_ -D_IPP_DEBUG -D_DISABLE_ALG_MD5_ -D_DISABLE_ALG_SM3_ -Wstrict-aliasing -g=2 OPTIMIZE=2 HTTPPROXY=false"

export OPTIMIZE="2"
export MODULES=true
export ARCH=x86

cd ${CUR_DIR}/../
git clone git://github.com/intel/safestringlib.git
export SAFESTRING_ROOT=${CUR_DIR}/../safestringlib
cd ${SAFESTRING_ROOT}
make 

cd ${CUR_DIR}
CFLAGS="${CFLAGS}" make 

}

do_install() {
    install -d "${D}/opt/sdo"
    install "${WORKDIR}/git/build/linux/debug/linux-client" "${D}/opt/sdo"
    install -d "${D}/opt/sdo/data"
    cp -r "${WORKDIR}/git/data/" "${D}/opt/sdo/"
    install -d "${D}/opt/sdo/data_bkp"
    cp -r "${WORKDIR}/git/data/" "${D}/opt/sdo/data_bkp"
}

do_package_qa[noexec] = "1"

INITSCRIPT_PACKAGES = "${PN}"
