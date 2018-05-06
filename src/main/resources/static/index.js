var currentDir = "";
var initDir = "";

$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/showall",
        type:'GET'
    }).then(function (data) {
        fillData(data);
        initDir=currentDir;
        changeBackVisibility();
        $(".backButton").click(goBack);
    });
});

//форматирование размера файла в удобочитаемый вид
function convertFileSize(bytes) {
    var kilo = 1024;
    if(Math.abs(bytes) < kilo) {
        return bytes + ' B';
    }
    var units = ['kB','MB','GB','TB'];
    var unitName = -1;
    do {
        bytes /= kilo;
        ++unitName;
    } while(Math.abs(bytes) >= kilo && unitName < units.length - 1);
    return bytes.toFixed(1)+' '+units[unitName];
}

//запрос текстового файла
function getTextFromFile() {
    var filename = this.getAttribute("value");
    //console.log(filename);
    var options = {"fileName": filename};
 //   console.log(options);
    $.ajax({
        url:"http://localhost:8080/gettextfromfile",
        type:"GET",
        data: options,
        error: function(error){
            console.log(error);
        }
    }).then(function (value) {
        console.log(value);
        alert(value.content);
    })
}

function getFilesFromDir() {
    var filename = this.getAttribute("value");
    console.log(filename);
    getFilesFromDirectory(filename);
}

function getFilesFromDirectory(filename) {
    //var dataToSend = {"filePath": filename}
    var addr = "http://localhost:8080/files/" + filename;
    console.log(filename);
    $.ajax({
        url: addr,
        type: 'GET',
     //   data: dataToSend,

        error: function (error) {
            console.log(error);
        }
    }).then(function (data) {
        fillData(data);
        changeBackVisibility();
    });
}

function fillData(data){
    //console.log(data);
    var rows = "";
    //заполнение данных о файлах
    $.each(data.files, function (index, obj) {
        rows += "<tr><td> " +
            (data.files[index].directory ? "yes" : "no") + "</td><td " +

            (data.files[index].directory ? " class='directory' value='" + data.files[index].absolutePath + "'" : "") +
            ">" + data.files[index].name + "</td>" +
            "<td>" + convertFileSize(data.files[index].fileSize) + "</td><td>" +
            new Date(data.files[index].creationDate).toLocaleString() + "</td><td>" +
            new Date(data.files[index].modificationDate).toLocaleString() + "</td>";
        if (data.files[index].text)
            rows += "<td><div class='showText' value='" + data.files[index].absolutePath + "'>show</div></td>";
        else
            rows += "<td>&nbsp;</td>"
        rows += "</tr>"
    });
    document.querySelector(".files tbody").innerHTML=rows;
    currentDir = data.path;


    //Отправляем по клику запрос для текстовых файлов
    var listText = document.querySelectorAll(".showText");
    // console.log(listText);
    //  console.log(listText.length);
    for (var i = 0; i < listText.length; i++) {
        listText[i].onclick = getTextFromFile;
    }
    //console.log(listText);

    var listDirs = document.querySelectorAll(".directory");
    for (var i = 0; i < listDirs.length; i++) {
        listDirs[i].onclick = getFilesFromDir;
    }


};

//видимость кнопки "назад"
function changeBackVisibility() {
    //console.log(currentDir);
    //console.log(initDir);
    if (currentDir==initDir)
        $(".backButton").hide();
    else
        $(".backButton").show();
}

function goBack() {
    //console.log(currentDir+"\\..");
    var dataToSend = {"filePath": pathBack(currentDir)};
    $.ajax({
        url: "http://localhost:8080/getfiles",
        type: 'GET',
        data: dataToSend,

        error: function (error) {
            console.log(error);
        }
    }).then(function (data) {
        fillData(data);
        changeBackVisibility();
    });
}

function pathBack(path) {
    var lastIndex = path.lastIndexOf("\\");
    return path.substring(0, lastIndex);
}