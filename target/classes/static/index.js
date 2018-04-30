$(document).ready(function() {

    $.ajax({
        url: "http://localhost:8080/showall",
        type:'GET'
    }).then(function(data) {
        var rows = "";
        //заполнение данных о файлах
        $.each(data.files, function (index, obj) {
            rows += "<tr><td> "+
                (data.files[index].directory?  "yes" : "no") + "</td><td "+

                (data.files[index].directory? " class='directory' value='"+data.files[index].absolutePath+"'": "") +
                ">"+ data.files[index].name +"</td>"+
                "<td>"+ convertFileSize(data.files[index].fileSize) + "</td><td>"+
                new Date(data.files[index].creationDate).toLocaleString() + "</td><td>"+
                new Date(data.files[index].modificationDate).toLocaleString() + "</td>+";
            if (data.files[index].text)
                rows+="<td><div class='showText' value='" + data.files[index].absolutePath + "'>show</div></td>";
            else
                rows +="<td>&nbsp;</td>"
            rows += "</tr>"
        });

        $(rows).appendTo(".files tbody");

        //Отправляем по клику запрос для текстовых файлов
        var listText = document.querySelectorAll(".showText");
        // console.log(listText);
        //  console.log(listText.length);
        for(var i=0; i<listText.length; i++) {
            listText[i].onclick=getTextFromFile;
        }
        console.log(listText);

        var listDirs = document.querySelectorAll(".directory");
        for (var i=0; i<listDirs.length; i++){
            listDirs[i].onclick=getFilesFromDir;
        }

    });
});

//форматирование размера файла в удобочитаемый вид
function convertFileSize(bytes) {
    var kilo = 1024;
    if(Math.abs(bytes) < kilo) {
        return bytes + ' B';
    }
    var units = ['kB','MB','GB','TB','PB','EB','ZB','YB'];
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
    console.log(filename);
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
    var dataToSend = {"filePath": filename}
    $.ajax({
        url: "http://localhost:8080/getfiles",
        type:'GET',
        data: dataToSend,

        error: function (error) {
            console.log(error);
        }
    }).then(function(data) {
        console.log(data);
        var rows = "";
        //заполнение данных о файлах
        $.each(data.files, function (index, obj) {
            rows += "<tr><td> "+
                (data.files[index].directory?  "yes" : "no") + "</td><td "+

                (data.files[index].directory? " class='directory' value='"+data.files[index].absolutePath+"'": "") +
                ">"+ data.files[index].name +"</td>"+
                "<td>"+ convertFileSize(data.files[index].fileSize) + "</td><td>"+
                new Date(data.files[index].creationDate).toLocaleString() + "</td><td>"+
                new Date(data.files[index].modificationDate).toLocaleString() + "</td>+";
            if (data.files[index].text)
                rows+="<td><div class='showText' value='" + data.files[index].absolutePath + "'>show</div></td>";
            else
                rows +="<td>&nbsp;</td>"
            rows += "</tr>"
        });

        $(".files tbody").replaceWith(rows);

        //Отправляем по клику запрос для текстовых файлов
        var listText = document.querySelectorAll(".showText");
        // console.log(listText);
        //  console.log(listText.length);
        for(var i=0; i<listText.length; i++) {
            listText[i].onclick=getTextFromFile;
        }
        console.log(listText);

        var listDirs = document.querySelectorAll(".directory");
        for (var i=0; i<listDirs.length; i++){
            listDirs[i].onclick=getFilesFromDir;
        }

    });
}