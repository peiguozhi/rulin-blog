const hljs = require("highlight.js/lib/core");

hljs.registerLanguage('c-like', require('highlight.js/lib/languages/c-like'));
hljs.registerLanguage("cpp", require("highlight.js/lib/languages/cpp"));
hljs.registerLanguage(
  "properties",
  require("highlight.js/lib/languages/properties")
);
hljs.registerLanguage("arduino", require("highlight.js/lib/languages/arduino"));
hljs.registerLanguage("xml", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("bash", require("highlight.js/lib/languages/bash"));
hljs.registerLanguage("c", require("highlight.js/lib/languages/c"));
hljs.registerLanguage("css", require("highlight.js/lib/languages/css"));
hljs.registerLanguage(
  "markdown",
  require("highlight.js/lib/languages/markdown")
);
hljs.registerLanguage("dns", require("highlight.js/lib/languages/dns"));
hljs.registerLanguage(
  "dockerfile",
  require("highlight.js/lib/languages/dockerfile")
);
hljs.registerLanguage("dos", require("highlight.js/lib/languages/dos"));
hljs.registerLanguage("go", require("highlight.js/lib/languages/go"));
hljs.registerLanguage("gradle", require("highlight.js/lib/languages/gradle"));
hljs.registerLanguage("groovy", require("highlight.js/lib/languages/groovy"));
hljs.registerLanguage("http", require("highlight.js/lib/languages/http"));
hljs.registerLanguage("ini", require("highlight.js/lib/languages/ini"));
hljs.registerLanguage("java", require("highlight.js/lib/languages/java"));
hljs.registerLanguage(
  "javascript",
  require("highlight.js/lib/languages/javascript")
);
hljs.registerLanguage("js", require("highlight.js/lib/languages/javascript"));
hljs.registerLanguage("json", require("highlight.js/lib/languages/json"));
hljs.registerLanguage("lua", require("highlight.js/lib/languages/lua"));
hljs.registerLanguage("nginx", require("highlight.js/lib/languages/nginx"));
hljs.registerLanguage(
  "powershell",
  require("highlight.js/lib/languages/powershell")
);
hljs.registerLanguage("python", require("highlight.js/lib/languages/python"));
hljs.registerLanguage("shell", require("highlight.js/lib/languages/shell"));
hljs.registerLanguage("sql", require("highlight.js/lib/languages/sql"));
hljs.registerLanguage("yaml", require("highlight.js/lib/languages/yaml"));
hljs.registerLanguage("vim", require("highlight.js/lib/languages/vim"));

module.exports = hljs;
