// com et trobes avui? , amb qui vols anar? , quan vols anar-hi?
// deshabilita checks si hem arribat al màxim d'opcions escollides 
function pregunta (classe,opcions) {
    let cont = 0;
    $(classe).each(function() {

        if ($(this).is(':checked')) { cont = cont + 1; }

        if (cont >= opcions) {
            $(classe).each(function() {
                if (!$(this).is(':checked')) {
                    $(this).attr("disabled", "disabled");
                }
            });
        } else {
            $(classe).each(function() {
                $(this).removeAttr('disabled');
            });
        }
    });
}


// quan vols anar-hi?
// deshabilita checks si hem arribat al màxim d'opcions escollides
// deshabilita checks si la primera opció està checked 
function pregunta3 () {
	let cont = 0;

	// si primera opció checked => resta d'opcions unchecked i disabled, i surt de la funció
    if ($('.pregunta3#horaris1').is(':checked')) {
        $('.pregunta3').each(function() {
            if (!$(this).is('#horaris1')) {
            	$(this).prop('checked',false);
                $(this).attr("disabled", "disabled");
            }
        });
        return;
    }   
    
    // si primera opció unchecked, activa la resta d'opcions
    if (!$('.pregunta3#horaris1').is(':checked')) {
		$('.pregunta3').each(function() {
            $(this).removeAttr('disabled');
        });
	} 
	
	$('.pregunta3').each(function() {

        if ($(this).is(':checked')) { cont = cont + 1; }

        if (cont >= 2) {
            $('.pregunta3').each(function() {
                if (!$(this).is(':checked')) 
                    $(this).attr("disabled", "disabled");
            });
        } else {
            $('.pregunta3').each(function() {
                $(this).removeAttr('disabled');
            });
        }
    });	
}

$(document).ready(function() {
	pregunta('.pregunta1',2);
	pregunta('.pregunta2',3);
	pregunta3();
});

$(document).on('click', '.pregunta1, .pregunta2, .pregunta3', function() {
	pregunta('.pregunta1',2);
	pregunta('.pregunta2',3);
	pregunta3();
});


// en informar d'un codi postal, carrega els seus municipis
function loadMunicipis(municipi) {
	var cPostal = $("form.container #codi option:selected").text();
	// buida select de municipis
	$('form.container #municipi').find('option').not(':first').remove();
	// surt si codi postal no informat
	if ($("form.container #codi").val()=="0") return;
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
				if (municipi!==undefined && municipi==data[i].id) {
					// si hi és, selecciona el municipi passat per paràmetre
					$('form.container #municipi').append($('<option>', {value:data[i].id, text:data[i].nom}).prop('selected', true));
				} else { 
					$('form.container #municipi').append($('<option>', {value:data[i].id, text:data[i].nom}));
				}
			}
			if (municipi==undefined && length==1) {
				// si no hi ha cap municipi passat per paràmetre i al desplegable només hi ha un municipi, ho selecciona
				$('form.container #municipi option:eq(1)').attr('selected', 'selected');
			}
		},
		error : function(jqXHR){
			$('form.container #municipi').siblings('obligatori').html("<span>Error en carregar els municipis</span>");
		}
	}); // fi ajax	
}

$("form.container #codi:not([readonly])").change(function() {
	// actualitza municipis en funció del codi postal (cp), si el desplegable per cp no està inhabilitat
	loadMunicipis();     
});
$(document).ready(function() {
	// carrega els municipis en funció del codi postal, i a més selecciona el municipi guardat a BBDD
	loadMunicipis($('form.container #municipi').val());
});
	
	
// botó Eliminar
$("form.container #eliminar").on("click", function() {
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
	var dataInici = "";
	var dataFi = "";
	// activitat destacada
    if ($("form.container #destacat").val() == "") {
        $('form.container #destacat').siblings('obligatori').html("<span>Camp obligatori!</span>");
        ok = false;
    } else {
        $('form.container #destacat').siblings('obligatori').html("<obligatori></obligatori>");
    }		 	
	// nom de la llista d'activitats
	if ($("form.container #name").val().length < 1) {
		$("form.container #name").siblings('obligatori').html("<span>Camp obligatori!</span>");
		ok = false;
	} else {
		$("form.container #name").siblings('obligatori').html("<obligatori></obligatori>");
	}
	// llista d'activitats
    if ($("form.container #dades").val() == "0") {
        $('form.container #dades').siblings('obligatori').html("<span>Camp obligatori!</span>");
        ok = false;
    } else {
        $('form.container #dades').siblings('obligatori').html("<obligatori></obligatori>");
    } 			
	// url
	if ($("form.container #url").val().length < 1) {
		$("form.container #url").siblings('obligatori').html("<span>Camp obligatori!</span>");
		ok = false;
	} else {
		$("form.container #url").siblings('obligatori').html("<obligatori></obligatori>");
	}			
	// descripcio
	if ($("form.container #descripcio").val().length < 1) {
		$("form.container #descripcio").siblings('obligatori').html("<span>Camp obligatori!</span>");
		ok = false;
	} else {
		$("form.container #descripcio").siblings('obligatori').html("<obligatori></obligatori>");
	}		
	// data d'inici
	if ($("form.container #dataInici").val().length < 1) {
		$("form.container #dataInici").siblings('obligatori').html("<span>Camp obligatori!</span>");
		ok = false;
	} else {
		$("form.container #dataInici").siblings('obligatori').html("<obligatori></obligatori>");
		dataInici = $("form.container #dataInici").val();
	}	
	// data fi
	if ($("form.container #dataFi").val().length < 1) {
		$("form.container #dataFi").siblings('obligatori').html("<span>Camp obligatori!</span>");
		ok = false;
	} else {
		$("form.container #dataFi").siblings('obligatori').html("<obligatori></obligatori>");
		dataFi = $("form.container #dataFi").val();
	}	
	// compara data d'inici i data fi
	if (dataInici!="" && dataFi!="" && dataInici>dataFi) {
		$("form.container #dataFi").siblings('obligatori').html("<span>La data de fi ha de ser más gran que la data d'inici!</span>");
		ok = false;
	}
	// dies de la setmana
	const days = ['#dl','#dt','#dc','#dj','#dv','#ds','#dg'];
	const selected = []; 
	for (let i = 0; i < days.length; i++) {
		 $(days[i]).each(function() {
			if ($(this).prop('checked')) {
				selected.push($(this).val());
			}
	 	});
	}
	if (selected.length==0) {
		$('form.container .dies').siblings('obligatori').html("<span>Has de seleccionar un com a mínim!</span>");
		ok = false;
	} else {
		$('form.container .dies').siblings('obligatori').html("<obligatori></obligatori>");
		$("form.container input#dies").val(selected.join(","));	
	}
	// codi postal
    if ($("form.container #codi").val() == "0") {
        $('form.container #codi').siblings('obligatori').html("<span>Camp obligatori!</span>");
        ok = false;
    } else {
        $('form.container #codi').siblings('obligatori').html("<obligatori></obligatori>");
    } 	
	// municipi
    if ($("form.container #municipi").val() == "0") {
        $('form.container #municipi').siblings('obligatori').html("<span>Camp obligatori!</span>");
        ok = false;
    } else {
        $('form.container #municipi').siblings('obligatori').html("<obligatori></obligatori>");
    } 
    // com et trobes avui? , amb qui vols anar? , quan vols anar-hi?
    const quest = ['.pregunta1', '.pregunta2', '.pregunta3'];
    for (let i = 0; i < quest.length; i++) {
        let check = false;
        $(quest[i]).each(function() {
            if ($(this).prop('checked')) {
                check = true;
            }
            if (!check) {
				$(this).siblings('obligatori').html("<span>Has de seleccionar un com a mínim!</span>");
            } else {
                $(this).siblings('obligatori').html("<obligatori></obligatori>");
            }
        });
        if (!check) ok=false;
    }
    // submit només si no hi ha errors
    if (ok)		{
		$("form.container").unbind('submit').submit();
		$("div.alert").removeClass("d-none");
    } else {
		$("div.alert").addClass("d-none");
		// scroll al primer missatge d'error
		$('html, body').scrollTop($("obligatori").offset().top);
	}
});