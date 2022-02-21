$(document).ready(function(){
	
	// camps formulari activitats, llistes, usuaris, continguts, rols -----------------------------
	
	// inhabilita els checkboxes readonly
	// http://comunidad.fware.pro/dev/html5/checkbox-de-solo-lectura-readonly/
	$(':checkbox[readonly=readonly]').click(function(){
		return false;         
	});

	// desfà els canvis als selects readonly	
	$('select[readonly=readonly]').change(function(){
		$(this).val($(this).attr('valor'));
	});	
	 
	 
	// Formulari dades de l'usuari ----------------------------------------------------------------
	
	// mostra formulari al front
	$("div#panellUsuariModal a.fw-bold").click(function(){
		$('#panellUsuariModal div.button.close').click(); 
		$('#dadesUsuariModal').modal('show');
	});
	
	// mostra formulari al back
	$("a#dadesUsuari").click(function(){ 
		$('#dadesUsuariModal').modal('show');
	});	
	
	// en tancar el formulari en fa un reset
	$("div#dadesUsuariModal div.button.close").click(function(){
		$('#dadesUsuariModal').modal('hide');
		$("div#dadesUsuariModal form#dadesUsuari input").val("");
		$("div#dadesUsuariModal form#dadesUsuari div.alert").remove();			
	});
	
	// submit del formulari
	$('div#dadesUsuariModal form#dadesUsuari').on('submit', function(e){
		// bloqueja el submit del formulari
		e.preventDefault();
		// lleva alerts anteriors
		$("div#dadesUsuariModal form#dadesUsuari div.alert").remove();		
		// recupera dades
  		var name = $('div#dadesUsuariModal form#dadesUsuari input#name').val();
  		var emailOld = $('div#dadesUsuariModal form#dadesUsuari input#emailOld').val();
  		var email = $('div#dadesUsuariModal form#dadesUsuari input#email').val();
  		var confirmEmail = $('div#dadesUsuariModal form#dadesUsuari input#confirmEmail').val();
  		var passwordOld = $('div#dadesUsuariModal form#dadesUsuari input#passwordOld').val();
  		var password = $('div#dadesUsuariModal form#dadesUsuari input#password').val();
  		var confirmPassword = $('div#dadesUsuariModal form#dadesUsuari input#confirmPassword').val();
  		$("div#dadesUsuariModal form#dadesUsuari").prepend("<div><div class='alert alert-info'>Enviant dades, per favor esperi ...</div></div>");
		// cridada ajax
		$.ajax({
			type : "POST",
			url : "/back/usuari/dadesUsuari",
			data : {
				name : name,
				emailOld : emailOld,
				email : email,
				confirmEmail : confirmEmail,
				passwordOld : passwordOld,
				password : password,
				confirmPassword : confirmPassword
			},
	       	success : function(data) {
				var json = JSON.parse(data);
				var errorsArray = json['errors'];
				var numErrors = errorsArray.length;
				var errors = "";
				$("div#dadesUsuariModal form#dadesUsuari div.alert").remove();				
				if (numErrors>0) {
					// pinta errors
					errorsArray.forEach(function(error) {
    					errors += "<div>"+error+"</div>";
					});
					$("div#dadesUsuariModal form#dadesUsuari").prepend("<div><div class='alert alert-danger'>"+errors+"</div></div>");			
	    		} else {
					$("div#dadesUsuariModal form#dadesUsuari").prepend("<div><div class='alert alert-success'>Les dades han estat actualitzades correctament</div></div>");
				}
   			
			},
			error : function(jqXHR){
				$("div#dadesUsuariModal form#dadesUsuari div.alert").remove();		
				$("div#dadesUsuariModal form#dadesUsuari").prepend("<div><div class='alert alert-danger'>ERROR "+jqXHR.status+"</div></div>");				
			}
		}); // fi ajax  
  	});	
		 
}); 