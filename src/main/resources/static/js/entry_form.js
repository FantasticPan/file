function validate(){
    var file=document.getElementById("file").value;
    if(file=="")
    {
        alert("请选择上传文件！");
        return false;
    }
    return true;
}