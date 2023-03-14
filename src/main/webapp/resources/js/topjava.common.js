let form;

function makeEditable(datatableApi) {
    ctx.datatableApi = datatableApi;
    form = $('#detailsForm');
    $(".delete").click(function () {
        if (confirm('Are you sure?')) {
            deleteRow($(this).closest('tr').attr("id"));
        }
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function add() {
    form.find(":input").val("");
    $("#editRow").modal();
}

function deleteRow(id) {
    $.ajax({
        url: ctx.ajaxUrl + id,
        type: "DELETE"
    }).done(function () {
        updateTable();
        successNoty("Deleted");
    });
}

function updateTable() {
    $.get(ctx.ajaxUrl, function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}

function save() {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        updateTable();
        successNoty("Saved");
    });
}

function filterMeal(event) {
    event.preventDefault();
    let startDate = $('#inputStartDate').val();
    let endDate = $('#inputEndDate').val();
    let startTime = $('#inputStartTime').val();
    let endTime = $('#inputEndTime').val();

    let data = {
        startDate: startDate,
        endDate: endDate,
        startTime: startTime,
        endTime: endTime
    };

    $.ajax({
        url: 'profile/meals/get-between',
        type: 'GET',
        data: data,
        success: function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
        }
    });
}

function cancelFilter() {
    updateTable();
}

function updateUserEnabled(enabled, id) {
    $.ajax({
        type: 'POST',
        url: ctx.ajaxUrl + id,
        data: { enabled: enabled },
    }).done(function () {
        updateTable();
        successNoty("Update enabled");
    });
}

let failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: " + jqXHR.status,
        type: "error",
        layout: "bottomRight"
    });
    failedNote.show()
}
