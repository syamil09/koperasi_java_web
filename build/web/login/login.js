/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    let userid, password;
    let btnLogin = $('#btnLogin');
    btnLogin.click(function(e) {
        e.preventDefault();
        console.log("klik login");
        userid = $('#userId').val();
        password = $('#password').val();
        
        if (userid === "") {
            alert("user id harus diisi!");
            $("#userId").focus();
        } else if (password === "") {
            alert("password harus diisi!");
            $('#password').focus();
        } else {
            btnLogin.attr('disabled','disabled');
            btnLogin.html(`
                        <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>&nbsp Login...
                        `);
        $.post('/PBO_koperasi/UserCtr',{
            page: 'login',
            userid: userid, 
            password: password
        },
        function(data) {
                btnLogin.removeAttr("disabled");
                btnLogin.html("Login");
                console.log(data)
                console.log(typeof data);
                if (typeof data !== "string") {
                    alert("Berhasil Login");
                    sessionStorage.setItem("data", JSON.stringify(data) );
                    window.location.href = "/PBO_koperasi/index.html";
//                    location.reload();
                } else if (data === "gagal") {
                    alert("userid atau password anda salah")
                } else if (data === "blokir") {
                    alert("Akun anda tidak aktif silahkan hubungi admnistrator")
                } else {
                    alert("terjadi kesalahan")
                }    
        }) /*end of ajax*/
        
        }
    })
    
})

