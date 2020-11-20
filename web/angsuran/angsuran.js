/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {

let noPinjaman, noAnggota, angsurKe, namaAnggota, sisaPinjaman, namaKaryawan, page;

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
                {
                    data:'besarAngsur',
                    defaultContent: '',
                    render: {
                        display: function(data, type, row) {
                            console.log("besarAngsur : "+data)
                            return formatRupiah(data.toString(), "Rp. ");
                        }
                    }
                },
                {
                    data:'sisaPinjaman',
                    defaultContent: '',
                    render: {
                        display: function(data, type, row) {
                            return formatRupiah(data.toString(), "Rp. ");
                        }
                    }
                },
                {data:'namaKaryawan'},
                {data: 'isLast', defaultContent: '', 'className': 'dt-right', 'orderable': false, 
                    render: {
                        display: function(data, type, row){
                        console.log('is last '+row.isLast)
                        if (row.isLast) {
                            return "<a class='btn btn-outline-warning btn-sm'"
                                    + "id = 'btnEdit'>Edit</a>"
                                    + "&nbsp;&nbsp;"
                                    + "<a class='btn btn-outline-danger btn-sm' "
                                    + "id='btnDel'>Hapus</a>";
                        }
                        
                        }
                    }
                            
                }
            ]
        })
    }
    
});

// Button Add clicked
$("#btnAdd").click(function(){
        clearForm();
        $("#modalAdd").modal('show');
        $("#titel1").show();
        $("#titel2").hide();
        $("#noPinjaman").prop('disabled', false);
        $("#btn-lookup-pinjaman").prop('disabled', false);
        page="tambah";
        console.log("add");
});    

// Save Data
$("#btnSave").click(function() {
    if($("#noPinjaman").val() == "") {
        alert("Nomor Pinjaman Harus Diisi!")
        $("#noPinjaman").focus()
    }
    else if($("#tglAngsuran").val() == "") {
        alert("Tanggal Angsuran Harus Diisi!")
        $("#tglAngsuran").focus()
    }
    else if($("#noKaryawan").val() == "") {
        alert("Nomor Karyawan Harus Diisi!")
        $("#noKaryawan").focus();
    }
    else {
        console.log("page : "+page)
        $.post('/PBO_koperasi/AngsuranCtr', {
            page: page,
            noPinjaman: $('#noPinjaman').val(),
            angsurKe: $('#angsurKe').val(),
            tglAngsur: $('#tglAngsuran').val(),
            besarAngsur: $('#besarAngsuran').val(),
            sisaPinjaman: $('#sisaPinjaman').val(),
            noKaryawan: $('#noKaryawan').val()
        }, function(data, status) {
                alert(data);
                if (data === "Data Berhasil diupdate" || data === "Data Berhasil disimpan") {location.reload(); }
        })
    }
    
});


// Edit Data
         $("#tabelangsuran tbody").on("click", "#btnEdit", function() {
             console.log("edit")
                $("#modalAdd").modal('show');
                $("#titel1").hide();
                $("#titel2").show();
                $("#noPinjaman").prop('disabled', true);
                $("#btn-lookup-pinjaman").prop('disabled', true);
                page = "tampil";
                
                let baris = $(this).closest('tr');
                let np = baris.find("td:eq(0)").text();
                let angsurke = baris.find("td:eq(1)").text();
                viewData(np, angsurke, "edit");
                page = "edit";
                
         });


//Delete Data
$('#tabelangsuran tbody').on('click', '#btnDel', function() {
             // get nik when clicked btn in the current row
             let baris = $(this).closest('tr');
             let np = baris.find("td:eq(0)").text();
             let angsuranKe = baris.find("td:eq(1)").text();
             page = 'hapus';
             console.log(np);
             if (confirm(`Anda yakin data  : ${np}, Angsuran Ke-${angsuranKe} akan dihapus ?`)) {
                 $.post("/PBO_koperasi/AngsuranCtr", {
                        page: page,
                        noPinjaman: np,
                        angsurKe: angsuranKe
                 },
                 function(data, status) {
                     alert(data);
                     location.reload();
                 });

             }
         });

// Fill data
function viewData(np, angsurke, type) {
    $.ajax({
        url: '/PBO_koperasi/AngsuranCtr',
        dataType: 'JSON',
        method: 'POST',
        data: {
            page: 'tampil',
            np: np,
            angsurKe: angsurke,
            type: type
        },
        success: function(data) {
            // cek jika null maka pinjaman sudah selesai/lunas
            if (data == null) {
                alert("Pinjaman ini sudah selesai!\nTidak bisa lagi menambah angsuran.");
            }
            else {      
                $("#noAnggota").val(data.noAnggota);
                $("#namaAnggota").val(data.namaAnggota);
                $("#besarAngsuran").val(data.besarAngsur);
                $("#besarAngsuranRp").val( formatRupiah(data.besarAngsur.toString(), 'Rp. ') );
                $("#angsurKe").val(data.jumlahAngsur);
                $("#sisaPinjaman").val(data.sisaPinjaman);
                $("#sisaPinjamanRp").val( formatRupiah(data.sisaPinjaman.toString(), 'Rp. ') );
                if (type == "edit") {
                    $("#noPinjaman").val(data.noPinjaman);
                    $("#noKaryawan").val(data.noKaryawan);
                    $("#namaKaryawan").val(data.namaKaryawan);
                    $("#tglAngsuran").val(data.tglAngsur);
                }
                console.log(data);
            }
        }
    })
}
//viewData("P-12", 1, "edit");

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
                let nopin = baris.find("td:eq(0)").text();
                let nama = baris.find("td:eq(1)").text();
                viewData(nopin, 0, "add");
                $("#noPinjaman").val(nopin);
                $("#modalLookupPinjaman").modal("hide");
        });
});

$("#noPinjaman").blur(function () {
    viewData(this.value, 0, "add")
})

$("#noKaryawan").blur(function () {
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
})

// Format Rupiah
function formatRupiah(angka, prefix) {
        var number_string = angka.replace(/[^,\d]/g, "").toString(),
        split = number_string.split(","),
        sisa = split[0].length % 3,
        rupiah = split[0].substr(0, sisa),
        ribuan = split[0].substr(sisa).match(/\d{3}/gi);

        // tambahkan titik jika yang di input sudah menjadi angka ribuan
        if (ribuan) {
            separator = sisa ? "." : "";
            rupiah += separator + ribuan.join(".");
        }

        rupiah = split[1] != undefined ? rupiah + "," + split[1] : rupiah;       
        return prefix == undefined ? rupiah : rupiah ? "Rp. " + rupiah : "";
}

// Clear Form
function clearForm() {
    $("#noPinjaman").val("");
    $("#noAnggota").val("");
    $("#tglAngsuran").val('');
    $("#angsurKe").val('');
    $("#namaAnggota").val('');
    $("#besarAngsuran").val('');
    $("#besarAngsuranRp").val('Rp. ');
    $("#sisaPinjaman").val('');
    $("#sisaPinjamanRp").val('Rp. ');
    $("#noKaryawan").val('');
    $("#namaKaryawan").val('');
}
})

