// submit modificació només si no hi ha errors	
$("form.container").on("submit", function(e) {
	e.preventDefault();
	var ok = true; 	
	// acompanyament
	if ($("form.container #acompanyament").val().length < 1) {
		$("form.container #acompanyament").siblings('obligatori').html("<span>Camp obligatori!</span>");
		ok = false;
	} else {
		$("form.container #acompanyament").siblings('obligatori').html("<obligatori></obligatori>");
	}
	// posició
    if ($("form.container #posicio").val() == "0") {
        $('form.container #posicio').siblings('obligatori').html("<span>Camp obligatori!</span>");
        ok = false;
    } else {
        $('form.container #posicio').siblings('obligatori').html("<obligatori></obligatori>");
    }   
    // submit només si no hi ha errors
    if (ok)		{
		$("form.container").unbind('submit').submit();
		$("div.alert").removeClass("d-none");
    } else {
		$("div.alert").addClass("d-none");
	}
});
