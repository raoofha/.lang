#!/bin/bash

lein cljsbuild once

#wget -c "https://coffeescript.org/browser-compiler-modern/coffeescript.js"
 wget -c "https://coffeescript.org/browser-compiler-legacy/coffeescript.js"

wget -c "https://github.com/brython-dev/brython/releases/download/3.10.5/Brython-3.10.5.zip"
unzip Brython-3.10.5.zip
rm Brython-3.10.5.zip
mv Brython-3.10.5/brython.js .
mv Brython-3.10.5/brython_stdlib.js .
rm -rf Brython-3.10.5
echo 'window.addEventListener("load", function(){ brython(1); })' > brython_load.js

#wget -c "https://github.com/microsoft/TypeScript/raw/main/lib/typescriptServices.js"
 wget -c "https://github.com/microsoft/TypeScript/raw/v4.6.2/lib/typescriptServices.js"


wget -c "https://cdn.rescript-lang.org/v9.1.2/compiler.js" -O rescript.js

#wget -c "https://tbfleming.github.io/cib/clang.wasm"
#wget -c "https://github.com/tbfleming/cib/archive/refs/heads/gh-pages.zip" -O clang.zip
#unzip clang.zip

#wget -c "https://registry-cdn.wapm.io/packages/_/clang/clang-0.1.0.tar.gz"

wget -c "https://github.com/binji/wasm-clang/archive/refs/heads/master.zip" -O wasm-clang.zip
unzip wasm-clang.zip
