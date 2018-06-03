function showMessage(message, type) {
    Noty.closeAll();
    new Noty({
        type: type,
        layout: 'topRight',
        text: message,
        closeWith: ['click'],
        modal: false
    }).show();
}

if (typeof jsf !== 'undefined') {
    jsf.ajax.addOnEvent(function (data) {
        var status = data.status;
        switch (status) {
            case "begin":
                $.blockUI();
                break;
            case "complete":
                $.unblockUI();
                break;
        }
    });

    jsf.ajax.addOnError(function (data) {
        showMessage(data.errorMessage, 'error');
    });
}
$.blockUI.defaults.message = '<h1>Processando</h1>';
$.blockUI.defaults.baseZ = 10000;
$(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);