/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {

let noPinjaman, noAnggota, angsurKe, namaAnggota, sisaPinjaman, namaKaryawan;

// get all data
$.ajax({
    url: '/PBO_koperasi/AngsuranCtr',
    method: 'GET',
    dataType: 'json',
    success: function(data) {
        $("#tabelangsuran").dataTable({
            serverside: true,
            processing: true, 
            data: data,
            sort: true,
            searching: true,
            paging: true,
            columns: [
                {data:'noPinjaman'},
                {data:'angsurKe'},
                {data:'noAnggota'},
                {data:'namaAnggota'},
                {data:'besarAngsur'},
                {data:'sisaPinjaman'},
                {data:'namaKaryawan'},
                {'data': null, 'className': 'dt-right', 'orderable': false, 'mRender': function(o){
                        return "<a class='btn btn-outline-warning btn-sm'"
                                    + "id = 'btnEdit'>Edit</a>"
                                    + "&nbsp;&nbsp;"
                                    + "<a class='btn btn-outline-danger btn-sm' "
                                    + "id='btnDel'>Hapus</a>";
                        }
                }
            ]
        })
    }
    
});

// Button Add clicked
$("#btnAdd").click(function(){
//        clearForm();
        $("#modalAdd").modal('show');
        $("#titel1").show();
        $("#titel2").hide();
        $("#nik").prop('disabled', false);
        $("input[name=persenBunga]").val('10');
        page="tambah";
        console.log("add");
});    

// Function load karyawan
function loadKaryawan() {
        loadKaryawan = 1;
        $.ajax({
                    url: "/PBO_koperasi/KaryawanCtr", 
                    method: "GET", 
                    dataType: "json",
                    success: function(data){
                                $("#tabelLookupKaryawan").dataTable({
                                serverside: true,
                                processing: true,
                                data: data,
                                sort: true,
                                searching: true,
                                paging: true,
                                columns: [
                                        {'data': 'nik', 'name': 'nik', 'type': 'string'},
                                        {'data': 'nama'},
                                        {'data': null, 'className': 'dt-right', 'mRender': function(o){
                                                return "<a class='btn btn-warning btn-sm'"
                                                + "id = 'btnInsertKaryawan'>Insert</a>";
                                            }
                                        }
                                    ]
                                });

                        }
        });
}

// Lookup Data Kayawan
$("#btn-lookup-karyawan").click(function() {
        $("#modalLookupKaryawan").modal('show');
        if (loadKaryawan != 1) {
                loadKaryawan();
        }
        $("#tabelLookupKaryawan tbody").on('click', '#btnInsertKaryawan', function() {
                    // get nik when clicked btn in the current row
                let baris = $(this).closest('tr');
                let no = baris.find("td:eq(0)").text();
                let nama = baris.find("td:eq(1)").text();
                $("input[name=nikKaryawan]").val(no);
                $("#namaKaryawan").val(nama);
                $("#modalLookupKaryawan").modal("hide");
        });
});

// Fill Name karyawan
$("input[name=nikKaryawan]").blur(function() {
        $.ajax({
                url: "/PBO_koperasi/KaryawanCtr",
                method: "GET", 
                dataType: "json",
                data: {
                   page: "tampil",
                   nik: this.value
                },
                success: function(data) {
                    if (Object.keys(data).length !== 0 && data.constructor === Object) $("#namaKaryawan").val(data.nama);
                    else $("#namaKaryawan").val('Data tidak ditemukan!');
                }
            });
        });

    
// Function load Pinjaman
function loadPinjaman() {
    loadPinjaman = 1;
        $.ajax({
                    url: "/PBO_koperasi/PinjamanCtr", 
                    method: "GET", 
                    dataType: "json",
                    success: function(data){
                                $("#tabelLookupPinjaman").dataTable({
                                serverside: true,
                                processing: true,
                                data: data,
                                sort: true,
                                searching: true,
                                paging: true,
                                columns: [
                                        {data: 'noPinjaman', 'name': 'nopinjaman', 'type': 'string'},
                                        {data: 'noAnggota'},
                                        {data: 'namaAnggota'},
                                        {data: null, orderable:false, 'className': 'dt-right', 'mRender': function(o){
                                                return "<a class='btn btn-warning btn-sm'"
                                                + "id = 'btnInsertPinjaman'>Insert</a>";
                                            }
                                        }
                                    ]
                                });

                        }
        });
}

// Lookup Data Pinjaman
$("#btn-lookup-pinjaman").click(function() {
        $("#modalLookupPinjaman").modal('show');
         loadPinjaman();
        $("#tabelLookupPinjaman tbody").on('click', '#btnInsertPinjaman', function() {
            console.log('insert pinjaman');
                // get nik when clicked btn in the current row
                let baris = $(this).closest('tr');
                let no = baris.find("td:eq(0)").text();
                let nama = baris.find("td:eq(1)").text();
                $("#noAnggota").val(no);
                $("#namaAnggota").val(nama);
                $("#modalLookupPinjaman").modal("hide");
        });
});

})

