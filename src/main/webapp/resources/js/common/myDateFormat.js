function myDataFormat(data){  // 使用方法:  $('#create-time').text(myDataFormat(new Date(award.createTime)));
    return data.getFullYear()+'-'+(data.getMonth()+1)+'-'+data.getDate() ;
}