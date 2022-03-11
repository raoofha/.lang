
let src = window.document.body.textContent;
let ext = location.href.substring(location.href.lastIndexOf("."));
//document.body.remove();
document.body.textContent = "";
if (ext == ".py")
{
  let script = document.createElement("script");
  script.type = "text/python";
  script.text = src;
  document.getElementsByTagName("html")[0].appendChild(script);
}
window.addEventListener("load", function()
{
  //console.log(window.document.body.textContent);
  switch(ext)
  {
    case ".cljs":
    {
      clojurescript.core.eval_str(src);
      break;
    }
    case ".coffee":
    {
      CoffeeScript.run(src, {filename: "coffeescript", sourceFiles: ["embedded"]});
      break;
    }
    case ".py":
    {
      brython(1);
      break;
    }
    case ".js":
    {
      eval(src);
      break;
    }
    case ".ts":
    {
      const jsCode = ts.transpile(src);
      eval(jsCode);
      break;
    }
    case ".res":
    {
      eval(rescript_compiler.make().rescript.compile(src).js_code);
      break;
    }
    case ".c":
    case ".cpp":
    {
      api.compileLinkRun(src);
      break;
    }
  }
});
