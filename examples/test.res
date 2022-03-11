
@val external write : string => unit = "document.write"
@val external version : string = "rescript_compiler.version"
write("hello from ReScript ")
write(version)
