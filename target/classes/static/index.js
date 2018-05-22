var currentDir = "";
var initDir = "";
var chosenFile = "";

var serverUrl = "http://localhost:8080"

$(document).ready(function () {

    getInitialData();

    console.log(document.querySelector(".files th").parentElement.parentElement.parentElement);

    $(".close").click(function () {
        this.modal('hide');
    });
    $("#move-copy-button").hide();


});

function getInitialData() {
    $(".alert").show().delay(3000).fadeOut();
    $.ajax({
        url: serverUrl + "/showall",
        type: 'GET'
    }).then(function (data) {
        fillData(data);
        initDir = currentDir;
        changeBackVisibility();
        $(".backButton").click(goBack);
        $(".alert").hide();
    });
}

//форматирование размера файла в удобочитаемый вид
function convertFileSize(bytes) {
    var kilo = 1024;
    if (Math.abs(bytes) < kilo) {
        return bytes + ' B';
    }
    var units = ['kB', 'MB', 'GB', 'TB'];
    var unitName = -1;
    do {
        bytes /= kilo;
        ++unitName;
    } while (Math.abs(bytes) >= kilo && unitName < units.length - 1);
    return bytes.toFixed(1) + ' ' + units[unitName];
}

//запрос текста из файла
function getTextFromFile() {
    var filename = this.parentElement.getAttribute("value");
    var options = {"fileName": filename};
    $(".alert").show().delay(3000).fadeOut();
    $.ajax({
        url: "http://localhost:8080/gettextfromfile",
        type: "GET",
        data: options,
        error: function (error) {
            console.log(error);
        }
    }).then(function (value) {
        console.log(value);

        //записываем текст в модальное окно
        document.querySelector('#modalText .modal-body p').innerText = value.content;
        document.querySelector('#modalText .modal-title').innerText = value.name;
        console.log(value);
        $('#modalText').modal('show');
    })
}

//запрос файлов из директории
function getFilesFromDir() {
    console.log(this);
    console.log(this.parentElement);
    var filename = this.getAttribute("value");
    console.log(filename);
    getFilesFromDirectory(filename);
}

//запрос файлов из директории
function getFilesFromDirectory(filename) {
    $(".alert").show().delay(3000).fadeOut();
    var addr = serverUrl + "/files/" + filename;
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

//заполнение таблицы
function fillData(data) {
    //console.log(data);
    var rows = "";
    //заполнение данных о файлах
    $.each(data.files, function (index, obj) {
        rows += "<tr><td><img src='/icons" +
            (data.files[index].directory ? "/folder.png" : (data.files[index].text ? "/textfile.png" : "/file.png")) + "'></td><td " +

            (data.files[index].directory ? " class='directory' value='" + data.files[index].absolutePath + "'" : "") +
            ">" + data.files[index].name + "</td>" +
            "<td>" + convertFileSize(data.files[index].fileSize) + "</td><td>" +
            new Date(data.files[index].creationDate).toLocaleString() + "</td><td>" +
            new Date(data.files[index].modificationDate).toLocaleString() + "</td>" +
            "<td value='" + data.files[index].absolutePath + "'>";
        if (data.files[index].text)
            rows += "<a class='btn btn-primary showText'>Show</a>";
        rows += "<a class='btn btn-primary renameFileButton'>Rename</a>" +
            "<a class='btn btn-primary moveFileButton'>Move</a>" +
            "<a class='btn btn-primary copyFileButton'>Copy</a>" +
            (data.files[index].directory ? "" : "<a class='btn btn-primary moveFileButton' href='" + serverUrl + "/download/" +
                data.files[index].absolutePath + "'> Download</a>") +
            "<a class='btn btn-danger btn-ok deleteFileButton'>Delete</a>";
        rows += "</td></tr>"
    });
    document.querySelector(".files tbody").innerHTML = rows;
    currentDir = data.path;


    //Отправляем по клику запрос для текстовых файлов
    var listText = document.querySelectorAll(".showText");
    // console.log(listText);
    //  console.log(listText.length);
    for (var i = 0; i < listText.length; i++) {
        listText[i].onclick = getTextFromFile;
    }

    //Удаляем файл по клику
    var deleteButtons = document.querySelectorAll(".deleteFileButton");
    for (var i = 0; i < deleteButtons.length; i++) {
        deleteButtons[i].onclick = deleteFileQuery;
    }

    //Переименовываем файл
    var renameButtons = document.querySelectorAll(".renameFileButton");
    for (var i = 0; i < renameButtons.length; i++) {
        renameButtons[i].onclick = renameFileQuery;
    }
    //Перемещение файла
    var moveButtons = document.querySelectorAll(".moveFileButton");
    for (var i = 0; i < moveButtons.length; i++) {
        moveButtons[i].onclick = moveFileQuery;
    }
    //Копирование файла
    var copyButtons = document.querySelectorAll(".copyFileButton");
    for (var i = 0; i < copyButtons.length; i++) {
        copyButtons[i].onclick = copyFileQuery;
    }
    //Для директорий - организуем их просмотр
    var listDirs = document.querySelectorAll(".directory");
    for (var i = 0; i < listDirs.length; i++) {
        listDirs[i].onclick = getFilesFromDir;
    }
};

//видимость кнопки "назад"
function changeBackVisibility() {
    //console.log(currentDir);
    //console.log(initDir);
    if (currentDir == initDir)
        $(".backButton").hide();
    else
        $(".backButton").show();
}

//назад на одну директорию
function goBack() {
    $(".alert").show().delay(3000).fadeOut();
    $.ajax({
        url: serverUrl + "/files/" + pathBack(currentDir),
        type: 'GET',
        //data: dataToSend,

        error: function (error) {
            console.log(error);
        }
    }).then(function (data) {
        fillData(data);
        changeBackVisibility();
    });
}

//расчёт пути, к которому нужно вернуться
function pathBack(path) {
    var lastIndex = path.lastIndexOf("\\");
    return path.substring(0, lastIndex);
}

//запрос на удаление файла
function deleteFileQuery() {
    $('#confirm-delete').modal('show');
    var filename = this.parentElement.getAttribute('value');
    //устанавливаем новый обработчик клика
    $('#confirm-delete .btn-danger').replaceWith($('#confirm-delete .btn-danger').clone());
    $('#confirm-delete .btn-danger').on('click', function () {
            var addr = "http://localhost:8080/files/" + filename;
            $(".alert").show().delay(3000).fadeOut();
            $.ajax({
                url: addr,
                type: 'DELETE',
                error: function (data) {
                    alert(data);
                }
            }).then(function (value) {
                alert(value);
                if (value == "Success")
                    getInitialData();
            })
            $('#confirm-delete').modal('hide');
        }
    )
}

//запрос на переименование файла
function renameFileQuery() {
    $('#renameFile').modal('show');
    var filename = this.parentElement.getAttribute('value');
    $('#renameFile .renameButton').replaceWith($('#renameFile .renameButton').clone());
    $('#renameFile .renameButton').on('click', function () {
        console.log($('#file-name').val());
        var addr = serverUrl + "/files/" + filename;
        var newName = $('#file-name').val();
        $(".alert").show().delay(3000).fadeOut();
        $.ajax({
            url: addr,
            type: 'POST',
            contentType: "application/json",
            //dataType: "json",
            data: JSON.stringify({"action": "rename", "newName": newName}),
            error: function (data) {
                console.log(data);
            }
        }).then(function (value) {
            alert(value);
            if (value == "Success")
                getInitialData();
        })


        $('#renameFile').modal('hide');
    })
}

//запрос на перемещение файла
function moveFileQuery() {
    chosenFile = this.parentElement.getAttribute('value');
    $("#move-copy-button").html("Move here");
    $("#move-copy-button").show();
    $('#move-copy-button').replaceWith($('#move-copy-button').clone()); //чтобы очистить обработчики
    $("#move-copy-button").on("click", function () {
        var addr = serverUrl+"/files/" + chosenFile;
        var path = currentDir;
        $(".alert").show().delay(3000).fadeOut();
        $.ajax({
            url: addr,
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify({"action": "move", "path": currentDir}),
            error: function (data) {
                console.log(data);
            }
        }).then(function (value) {
            alert(value);
            if (value == "Success")
                getFilesFromDirectory(currentDir);
        })
        $("#move-copy-button").hide();
    })
}

//копирование файла
function copyFileQuery() {
    chosenFile = this.parentElement.getAttribute('value');
    $("#move-copy-button").html("Copy here");
    $("#move-copy-button").show();
    $('#move-copy-button').replaceWith($('#move-copy-button').clone()); //чтобы очистить обработчики
    $("#move-copy-button").on("click", function () {
        var addr = serverUrl+"/files/" + chosenFile;
        var path = currentDir;
        $(".alert").show().delay(3000).fadeOut();
        $.ajax({
            url: addr,
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify({"action": "copy", "path": currentDir}),
            error: function (data) {
                console.log(data);
            }
        }).then(function (value) {
            alert(value);
            if (value == "Success")
                getFilesFromDirectory(currentDir);
        })
        $("#move-copy-button").hide();
    })
}