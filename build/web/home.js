/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//$(document).ready(function() {
    console.log(sessionStorage.getItem('data'))
    console.log($("#welcome"))
    $("#welcome").html(`Halo, ${sessionStorage.getItem("data.nama")}`);
//})

