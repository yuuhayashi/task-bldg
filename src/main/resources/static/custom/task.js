var queryString = window.location.search;
var queryObject = new Object();
if(queryString){
	queryString = queryString.substring(1);
	var parameters = queryString.split('&');
	  
	for (var i = 0; i < parameters.length; i++) {
		var element = parameters[i].split('=');
	    
		var paramName = decodeURIComponent(element[0]);
		var paramValue = decodeURIComponent(element[1]);
	
		queryObject[paramName] = paramValue;
	}
}
  
const citycode = queryObject['citycode'];
const meshcode = queryObject['meshcode'];
const op = queryObject['op'];

function loadMap() {
	const div = document.getElementById("changeSet");
	
	const lab1 = document.createElement("label");
	lab1.setAttribute("for", "changeSet");
	lab1.setAttribute("th:text", "#{changeSet}");
	
	const inp1 = document.createElement("input");
	inp1.setAttribute("type", "text");
	inp1.setAttribute("class", "form-control");
	inp1.setAttribute("th:errorclass", "is-invalid");
	inp1.setAttribute("th:field", "*{changeSet}");
	
	const d1 = document.createElement("div");
	d1.setAttribute("class", "invalid-feedback");
	d1.setAttribute("th:errors", "*{changeSet}");
	
	const inp2 = document.createElement("input");
	inp2.setAttribute("type", "hidden");
	inp2.setAttribute("th:field", "*{changeSet}");
	inp2.setAttribute("th:value", "*{changeSet}");
	
	if (op == "DONE") {
		div.appendChild(lab1);
		div.appendChild(inp1);
		div.appendChild(d1);
	}
	else {
		div.appendChild(inp2);
	}
}
