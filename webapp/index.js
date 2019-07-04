function getFiles() {
	// 指定url
	var url = "http://localhost:8080/D:/succezIDE/workspace/study";
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.open("get", url, true);
	//发送请求
	xmlHttp.send();
	// 响应函数
	xmlHttp.onreadystatechage = function() {
		// 如果响应成功，就获取json
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			//获取响应数据
			var result = xmlHttp.responseText;
			// 使用eval将返回的字符串转换成js对象
			var files = eval("(" + result + ")");
			// 获取files的信息
			for (var i = 0; i < results.length; i++) {
				// 将每个file的信息填充到html中
				var file = files[i];
				appendRow(file[0], file[1], file[2]);
			}
		}
	}
}

// 添加新行
function appendRow(name, size, date) {
	//在表格中插入行
	var ui = document.getElementById("files");
	var newRow = ui.insertRow(ui.rows.length);

	// 添加新的单元格
	newRow.insertCell(0).innerHTML = name;
	newRow.insertCell(1).innerHTML = size;
	newRow.insertCell(2).innerHTML = date;
}
