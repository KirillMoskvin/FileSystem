$(document).ready(function() {

    $.ajax({
        url: "http://localhost:8080/showall",
        type:'GET'
    }).then(function(data) {
        var rows = "";
        console.log(data);
        console.log(rows);
        $.each(data.files, function (index, obj) {
            rows += "<tr><td>"+
                (data.files[index].directory? "yes" : "no") + "</td><td>" +
                data.files[index].name + "</td><td>"+
                convertFileSize(data.files[index].fileSize) + "</td><td>"+
                new Date(data.files[index].creationDate).toLocaleString() + "</td><td>"+
                new Date(data.files[index].modificationDate).toLocaleString() + "</td></tr>";
        });

        $("#files tbody").append(rows);
        $(rows).appendTo(".files tbody");
        $(".sm").innerText += data;
        $("#sm").innerText += 2;
        console.log(data);
        console.log(rows);
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
//формат даты в удобный вид
function formatDate(date) {

}