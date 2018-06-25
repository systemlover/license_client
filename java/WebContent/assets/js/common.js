$(document).ready( function () {
    $('.datatable').DataTable({
        columnDefs: [{
            targets: [0],
            visible: false,
            searchable: false,
        }],
        order: [[ 0, 'desc' ]]
    });
});
