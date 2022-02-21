// botó Eliminar
$("#eliminar").on("click", function() {
	// mostra requadre per confirmar l'eliminar
	if ($("form.container #confirmar").hasClass("d-none")) {
		$("form.container #confirmar").addClass("d-block");
		$("form.container #confirmar").removeClass("d-none");
	}
	// esborra si confirmat
	if ($("form.container #confirmar").hasClass("d-block") && $("form.container #checkConfirmar").prop("checked")) {
		window.location.href = $("form.container #eliminarOk").attr('href');
	}
});

	
// submit modificació només si no hi ha errors	
$("form.container").on("submit", function(e) {
	e.preventDefault();
	var ok = true; 	
	// rol
    if ($("form.container #role").val() == "0") {
        $('form.container #role').siblings('obligatori').html("<span>Camp obligatori!</span>");
        ok = false;
    } else {
        $('form.container #role').siblings('obligatori').html("<obligatori></obligatori>");
    }   
    // submit només si no hi ha errors
    if (ok)		{
		$("form.container").unbind('submit').submit();
		$("div.alert").removeClass("d-none");
    } else {
		$("div.alert").addClass("d-none");
	}
});
