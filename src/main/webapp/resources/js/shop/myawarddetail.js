$(function () {
    // 从地址栏中获取userAwardId
    var userAwardId = getQueryString('awardId');
    // 根据userAwardId获取用户奖品映射信息
    var awardUrl = '/o2o/shopAdmin/getawardbyid?awardId='
        + userAwardId;

    $
        .getJSON(
            awardUrl,
            function (data) {
                if (data.success) {
                    // 获取奖品信息并显示
                    var award = data.award;
                    $('#award-img').attr('src', getContextPath() + award.awardImg);
                    //$('#create-time').text(new Date(award.createTime).Format("yyyy-MM-dd"));
                    $('#create-time').text(myDataFormat(new Date(award.createTime)));
                    $('#award-name').text(award.awardName);
                    $('#award-desc').text(award.awardDesc);
                }
            });
    $.init();
});
