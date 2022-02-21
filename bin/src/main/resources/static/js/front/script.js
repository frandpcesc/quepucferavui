$(document).ready(function() {
	
	// Pregunta 4 ---------------------------------------------------------------------------------
		
	// en informar d'un codi postal, 
	// carrega municipis en funció del codi postal (només si està informat)
	$("#cPostal").change(function() {
		var cPostal = $("#cPostal").val(); 
		// buida select de municipis
		$('#municipi').find('option').not(':first').remove();
		// surt si codi postal no informat
		if (cPostal=="") return;
		// cridada ajax
		$.ajax({
			type : "POST",
			url : "/front/loadMunicipis",
			data : {
				cPostal : cPostal		
			},
	       	success : function(data) {
				var length = data.length;
				// afegeix municipis
				for (var i = 0; i < length; i++) {
					$('#municipi').append(
						$('<option>', {value:data[i].id, text:data[i].nom}).prop('selected', true)
					);
				}
				// mostra botó 'Ara l'última' només si informats codi postal i municipi
				/*
		        if ($("#cPostal").val() != "" && $("#municipi").val() != "Municipi") {
        		    $("#btn-gracies").css("display", "none");
            		$("#btn-somhi").css("display", "inline-flex");
            	}
            	*/
			},
			error : function(jqXHR){
				switch (jqXHR.status) {
					case 406:
						// error El codi postal no té un format vàlid
					 	$("#cPostal").css({ "border-color": "#dc3545", "border-width": "2px" });
					 	$("#municipis").css({ "border-color": "#adadad", "border-width": "1px" });
					 	$("#txt-error span").html("El codi postal no té un format vàlid");
					 	$("#txt-error").css("display", "block");
						break;
					case 404:
						// error Segur que el codi postal és correcte? No en té cap municipi!
					 	$("#cPostal").css({ "border-color": "#dc3545", "border-width": "2px" });
				 	 	$("#municipis").css({ "border-color": "#dc3545", "border-width": "2px" });
					 	$("#txt-error span").html("Segur que el codi postal és correcte? No en té cap municipi!");
					 	$("#txt-error").css("display", "block");				
						break;
					default:
						break;
				}
			}
		}); // fi ajax     
	});
	
	// en canviar de municipi, vigila que estiguin informats codi postal i municipi,
	// per mostrar el botó 'Ara l'última'
/*
    $("#municipi").change(function() {
        if ($("#cPostal").val() != "" && $("#municipi").val() != "Municipi") {
            $("#btn-gracies").css("display", "none");
            $("#btn-somhi").css("display", "inline-flex");
        }

    });    
*/

	// en pijtar el botó 'Ara l'última' mostra la pregunta 'Per on vols anar?' 
	// només si informats codi postal i municipi
    $("#btn-somhi").on("click", function() {
        if($("#cPostal").val() == "" || $("#municipi").val() == "Municipi" || $("#municipi").val() == "")  {
          
            $("#txt-error").css("display", "block");

            if ($("#cPostal").val() == "") {
				$("#txt-error span").html("El codi postal o municipi no és correcte");
                $("#cPostal").css({ "border-color": "#dc3545", "border-width": "2px" });
            } else {
                $("#cPostal").css({ "border-color": "#adadad", "border-width": "1px" });
            }

            if ($("#municipis").val() == "Municipi" || $("#municipi").val() == "") {
				$("#txt-error span").html("El codi postal o municipi no és correcte");
                $("#municipis").css({ "border-color": "#dc3545", "border-width": "2px" });
            } else {
                $("#municipis").css({ "border-color": "#adadad", "border-width": "1px" });
            }
            
		} else {
			// per defecte seleccionada la primera opció quan va a la darrera pregunta
			$("input:radio[name=distancia]:first").attr('checked', true);
			$(".ocult").css("display", "none");
        	$(".forms_onets").fadeOut();
        	$(".forms_onets").fadeOut(2000);
        	setTimeout(function() {
            $(".ocult").fadeIn();
            $(".ocult").fadeIn(2000);
        	}, 600);
		}         

    });


    // Modal usuari -------------------------------------------------------------------------------
    $("#usuariModal form a.boto").on("click", function() {
        $("#noLogat").fadeOut();
        $("#noLogat").fadeOut(2000);
        setTimeout(function() {
            $("#logat").fadeIn();
            $("#logat").fadeIn(2000);
        }, 600);
    });


    // Modal enviar a un amic ---------------------------------------------------------------------
    $("#amicModal form a").on("click", function() {
        $("#b-enviar").fadeOut();
        $("#b-enviar").fadeOut(2000);
        setTimeout(function() {
            $("#fet").fadeIn();
            $("#fet").fadeIn(2000);
        }, 600);
    });
    
    
   // Boto compartir Activitat --------------------------------------------------------------------
	$("#botoCompartir").on("click", function(){
		const nomact = $(this).attr("data-act-nom");
		const infoact = $(this).attr("data-act-id");
		$("#textNom").text(nomact);
		$("#activitatCompartir").val(infoact);
	});	
    
    // compartir activitat
	$('div#amicModal form#compartirActivitat').on('submit', function(e){
		// lleva alerts anteriors
		$("div#amicModal form#compartirActivitat div.alert").remove();
		// recupera dades
		var nom = $('form#compartirActivitat input#nomCompartir').val();
  		var email = $('form#compartirActivitat input#emailCompartir').val();
  		var activitatCompartir = $('form#compartirActivitat input#activitatCompartir').val();		
  		if (nom=="" || email=="") {
  			var error = "El nom i el correu electrònic són obligatoris";
  			$("div#amicModal form#compartirActivitat").prepend("<div><div class='alert alert-danger'>"+error+"</div></div>");
  			return false;
  		}		
		//bloqueja el submit del formulari
		e.preventDefault();  		
		$("div#amicModal form#compartirActivitat").prepend("<div><div class='alert alert-info'>Enviant activitat als teus amics/amigues...</div></div>");  		
		$.ajax({
			type : "POST",
			url : "/front/compartir",
			data : {
				nom : nom,
				email: email,
				activitatCompartir: activitatCompartir		
			},
	       	success : function(data) {
				$("div#amicModal form#compartirActivitat div.alert").remove();
				$("div#amicModal form#compartirActivitat").prepend("<div><div class='alert alert-success'>Activitat enviada!</div></div>");
			},
			error : function(jqXHR){
				$("div#amicModal form#compartirActivitat div.alert").remove();
				$("div#amicModal form#compartirActivitat").prepend("<div><div class='alert alert-danger'>ERROR "+jqXHR.status+"</div></div>");
			}
		}); // fi ajax   
	});     
    
    
    // Preguntes pàgina índex ---------------------------------------------------------------------
    // Per defecte seleccionada la primera opció de cada pregunta
    // (excepte la darrera, que n'és opcional)
    $("input:radio[name=anims]:first").attr('checked', true);
    $("input:radio[name=companys]:first").attr('checked', true);
    $("input:radio[name=horaris]:first").attr('checked', true);
   
    
    // Formulari login ----------------------------------------------------------------------------
	
	// iframe ocult per fer el login (simula login via ajax)
	$('div#usuariModal form#login').on('submit', function(e){
		// lleva alerts anteriors
		$("div#usuariModal form#login div.alert").remove();
		// recupera dades
  		var username = $('div#usuariModal form#login input#username').val();
  		var password = $('div#usuariModal form#login input#password').val();
  		if (username=="" || password=="") {
  			var error = "El correu electrònic i el password són obligatoris";
  			$("div#usuariModal form#login").prepend("<div><div class='alert alert-danger'>"+error+"</div></div>");
  			return false;
  		}
  		$("div#usuariModal form#login").prepend("<div><div class='alert alert-info'>Enviant credencials, per favor esperi ...</div></div>");
		// clona formulari a l'iframe
		$('div#usuariModal form#login').clone(true).appendTo($('iframe#ifrmLogin').contents().find('body'));
		// bloqueja el submit del formulari
		e.preventDefault();
  		// clona usuari / password
  		$('iframe#ifrmLogin').contents().find('form#login input#username').val(username);
  		$('iframe#ifrmLogin').contents().find('form#login input#password').val(password);
  		// desbloqueja el submit del formulari que hi ha a l'iframe, i ho submita
   		$('iframe#ifrmLogin').contents().find('form#login').unbind("submit");
  		$('iframe#ifrmLogin').contents().find('form#login').submit();  		
  	});
  
  	// torna resultat del login fet a l'iframe
	$('iframe#ifrmLogin').on("load", function() {
    	var error = $('iframe#ifrmLogin').contents().find('div#usuariModal form#login div.alert.alert-danger').html();
    	if (typeof(error)!="undefined") {
			// error en fer el login => copia el missatge d'error al formulari de la pàgina
			$("div#usuariModal form#login div.alert").remove();
			$("div#usuariModal form#login").prepend("<div><div class='alert alert-danger'>"+error+"</div></div>");
		} else {
			// mostra missatge de logat
			$("div#usuariModal div#noLogat").hide(); 
			$("div#usuariModal div#logat").show();
			$("div#usuariModal div.button.close").click(function(){location.reload()});
			$("div#usuariModal div#logat a.boto").click(function(){location.reload()});
		}
	});
	
	
	// Formulari registre -------------------------------------------------------------------------
	
	// mostra formulari
	$("div#usuariModal a.fw-bold").click(function(){
		$('#usuariModal div.button.close').click(); 
		$('#registreModal').modal('show');
	});
	
	// tanca formulari
	$("div#registreModal div.button.close").click(function(){
		$('#registreModal').modal('hide');
	});
	
	// submit del formulari
	$('div#registreModal form#registre').on('submit', function(e){
		// bloqueja el submit del formulari
		e.preventDefault();
		// lleva alerts anteriors
		$("div#registreModal form#registre div.alert").remove();		
		// recupera dades
  		var name = $('div#registreModal form#registre input#name').val();
  		var email = $('div#registreModal form#registre input#email').val();
  		var confirmEmail = $('div#registreModal form#registre input#confirmEmail').val();
  		var password = $('div#registreModal form#registre input#password').val();
  		var confirmPassword = $('div#registreModal form#registre input#confirmPassword').val();
  		var terms = $('div#registreModal form#registre input#terms').is(":checked");
  		$("div#registreModal form#registre").prepend("<div><div class='alert alert-info'>Enviant credencials, per favor esperi ...</div></div>");
		// cridada ajax
		$.ajax({
			type : "POST",
			url : "/front/registration",
			data : {
				name : name,
				email : email,
				confirmEmail : confirmEmail,
				password : password,
				confirmPassword : confirmPassword,
				terms : terms,
				role: '1'					// ROL_USER
//				accountNonLocked: true		// usuari actiu
			},
	       	success : function(data) {
				var json = JSON.parse(data);
				var errors = "";
				var numErrors = Object.keys(json).length;
				$("div#registreModal form#registre div.alert").remove();				
				if (numErrors>0) {
					// pinta errors
					for (var key in json) {errors = "<div>"+json[key]+"</div>" + errors;}
					$("div#registreModal form#registre").prepend("<div><div class='alert alert-danger'>"+errors+"</div></div>");			
	    		} else {
					$("div#registreModal form#registre").prepend("<div><div class='alert alert-success'>Us heu registrat amb èxit a la nostra fantàstica aplicació!</div></div>");
				}
   			
			},
			error : function(jqXHR){
				$("div#registreModal form#registre div.alert").remove();		
				$("div#registreModal form#registre").prepend("<div><div class='alert alert-danger'>ERROR "+jqXHR.status+"</div></div>");				
			}
		}); // fi ajax  
  	});
  			
	
    // Formulari de contacte ----------------------------------------------------------------------
    
	$('div#contacteModal form#contactar').on('submit', function(e){
		// lleva alerts anteriors
		$("div#contacteModal form#contactar div.alert").remove();
		// recupera dades
		var nom = $('div#contacteModal form#contactar input#nom').val();
  		var telefon = $('div#contacteModal form#contactar input#telefon').val();		
  		if (nom=="" || telefon=="") {
  			var error = "El nom i el telèfon són obligatoris";
  			$("div#contacteModal form#contactar").prepend("<div><div class='alert alert-danger'>"+error+"</div></div>");
  			return false;
  		}  		
		// bloqueja el submit del formulari
		e.preventDefault();  		
		$("div#contacteModal form#contactar").prepend("<div><div class='alert alert-info'>Enviant avís, per favor esperi ...</div></div>");  		
		$.ajax({
			type : "POST",
			url : "/front/contactar",
			data : {
				nom : nom,
				telefon: telefon		
			},
	       	success : function(data) {
				$("div#contacteModal form#contactar div.alert").remove();
				$("div#contacteModal form#contactar").prepend("<div><div class='alert alert-success'>Avís enviat!</div></div>");
			},
			error : function(jqXHR){
				$("div#contacteModal form#contactar div.alert").remove();
				$("div#contacteModal form#contactar").prepend("<div><div class='alert alert-danger'>ERROR "+jqXHR.status+"</div></div>");
			}
		}); // fi ajax   
	});
	
	
	// Boto Activitates Preferides ----------------------------------------------------------------	
	//guardar a favorits
   $("#botoFavorit").on("click", function() {
		const botoCor = this;
		const like = this.dataset.like;
		const activitatId = this.dataset.actId;
		var $this = $(this);
		// Té like
		if( like !== "0"){
			borrarLike(activitatId, botoCor);
			$this.removeClass('like');
		}
		// No té like
		else {
			guardarLike(activitatId, botoCor);
			$this.addClass('like');
		}
	});
    
});


function guardarLike(activitatId, botoCor) {
	$.ajax({
			type : "POST",
			url : "/front/like",
			data : {
				activitatId: activitatId
			},
	       	success : function(data) {
				console.table(data);
				botoCor.dataset.like = "1"
			},
			error : function(jqXHR){
				console.warn(jqXHR);
			}
		}); 
}

function borrarLike(activitatId, botoCor) {
	$.ajax({
			type : "DELETE",
			url : "/front/like",
			data : {
				activitatId: activitatId
			},
	       	success : function(data) {
				console.table(data);
				botoCor.dataset.like = "0"
			},
			error : function(jqXHR){
				console.warn(jqXHR);
			}
		}); 
}
