(function () {
    var jCollage = null;
    var proximaPagina;
    var qtdFotos = -1;
    var qtdFotosCarregadas = 0;
    var debug = false;

    function ajaxCall(method, path, callback, errorMessage, requestData) {
        $("body").addClass('processando');
        $.ajax({
            url: location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '') + path,
            type: method,
            contentType: "application/json",
            dataType: 'json',
            data: requestData,
            statusCode: {
                400: function () {
                    if (debug) alert('BadRequestException');
                    window.top.location = '/erro';
                },
                401: function () {
                    if (debug) alert('UnauthorizedException');
                    window.top.location = '/erro';
                },
                403: function () {
                    if (debug) alert('ForbiddenException');
                    window.top.location = '/erro';
                },
                404: function () {
                    if (debug) alert('NotFoundException');
                    window.top.location = '/erro';
                },
                409: function () {
                    if (debug) alert('ConflictException');
                    window.top.location = '/erro';
                },
                500: function () {
                    if (debug) alert('InternalServerErrorException');
                    window.top.location = '/erro';
                },
                503: function () {
                    if (debug) alert('ServiceUnavailableException');
                    window.top.location = '/erro';
                }
            },
            success: function (data) {
                callback(data);
                $('body').removeClass('processando');
            },
            error: function () {
                $('body').removeClass('processando');
                alert(errorMessage);
                window.top.location = '/erro';
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader('token-uuid', $("#token-uuid").val());
            }
        });

    }

    function checkPermissions(perms, callback, failCallback) {
        FB.api('/me/permissions', function (response) {
            var fbPerms = response.data[0];
            var haveAllPermissions = true;

            if (typeof perms === 'string') {
                perms = [ perms ];
            }

            for (var i in perms) {
                if (fbPerms[perms[i]] == null) {
                    haveAllPermissions = false;
                    break;
                }
            }

            if (haveAllPermissions) {
                callback();
            } else {
                failCallback();
            }
        });
    }

    function cursoresProcessamento() {
        $('body, .fotos img').addClass('processando');
    }

    function cursoresPadrao() {
        $('body, .fotos img').removeClass('processando');
    }

    function enviaColagem() {
        jCollage.redraw();
        var canvas = document.getElementById('collage');
        var dataURL = canvas.toDataURL('image/jpeg');
        var requestData = JSON.stringify({ dataURL: dataURL });

        function generateRequest() {
            ajaxCall('POST', '/_ah/api/sfcb/v1/foto',
                function (data) {
                    var idsDaPostagem = data.postId.split("_");
                    $("#idUsuario").val(idsDaPostagem[0]);
                    $("#idPost").val(idsDaPostagem[1]);
                    $("#idImagem").val(data.id);
                    if (debug) alert("id usuario: " + $("#idUsuario").val()
                        + "\nid do post: " + $("#idPost").val()
                        + "\nid da imagem: " + $("#idImagem").val());
                    $("#formSucesso").submit();
                },
                'Erro ao publicar foto',
                requestData);
        }

        // Verifica permissão
        checkPermissions("publish_actions",
            // Em caso de sucesso cria o request
            generateRequest,
            // Em caso de fracasso
            function () {
                // requisita a permissão
                FB.login(function () {
                    // Verifica a permissão novamente
                    checkPermissions("publish_actions",
                        // Em caso de sucesso cria o request
                        generateRequest,
                        // Em caso de fracasso notifica o usuário
                        function () {
                            alert("Para postar a colagem o SCFB precisa da sua permissão.\n\n"
                                + "Para continuar clique novamente em POSTAR e autorize a\n"
                                + "aplicação a postar em seu nome. ");
                            $("#gerarColagem").one('click', enviaColagem);
                        });
                }, {scope: "publish_actions"});
            });
    }

    function carregarImagem(e) {
        cursoresProcessamento();

        var file = e.target.files[0];

        // Only process image files.
        if (!file) {
            cursoresPadrao();
        } else if (!file.type.match('image.*')) {
            alert("Formato não suportado");
            cursoresPadrao();
        }
        else {
            var reader = new FileReader();
            reader.onload = function (event) {
                var img = new Image();
                img.onload = function () {
                    jCollage.addLayer(img).setTitle('Camada ');
                    updateLayers(jCollage.getLayers());
                    $("#layer_" + (jCollage.getLayers().length - 1)).addClass("selected");
                    cursoresPadrao();
                }
                img.src = event.target.result;
            }
            reader.readAsDataURL(file);
        }
    }

    $(document).ready(function () {

        $('#mycarousel').jcarousel({
            itemLoadCallback: mycarousel_itemLoadCallback,
            scroll: 5
        });

        jCollage = new Collage("#collage");
        jCollage.setBackgroundColor("#fff");

        $(document).on("click", ".fotos img", function () {
            cursoresProcessamento();
            var img = $(this);
            var imgUrl = document.createElement("img");
            imgUrl.onload = function () {
                jCollage.addLayer(imgUrl).setTitle(img.attr("title"));
                updateLayers(jCollage.getLayers());
                $("#layer_" + (jCollage.getLayers().length - 1)).addClass("selected");
                cursoresPadrao();
            };
            imgUrl.src = img.data("proxy-url");
        });

        $(document).on("click", ".camadas .layer", function () {
            $(".camadas .layer").removeClass("selected");
            $(this).addClass("selected");
            setSettings($(this).attr("id").substr(6));
        });

        $(".camadas .background .visible").click(function () {
            if ($(this).html() == "") {
                jCollage.setBackgroundImage($(".camadas .background img")[0]);
                $(this).html("&radic;");
            } else {
                jCollage.setBackgroundImage(null);
                $(this).html("");
            }
        });

        $(document).on("click", ".camadas .layer .visible", function () {
            if ($(this).html() == "") {
                $(this).html("&radic;");
            } else {
                $(this).html("");
            }
            jCollage.getLayer($(this).parent().attr("id").substr(6)).toggleVisible();
            jCollage.redraw();
        });

        $(".options select[name=shadow]").change(function () {
            if (getSelectedLayer() != null) {
                if ($(".options select[name=shadow]").val() == "true") {
                    getSelectedLayer().setShadow(true);
                } else {
                    getSelectedLayer().setShadow(false);
                }
                jCollage.redraw();
            }
        });

        $(".options select[name=opacity]").change(function () {
            if (getSelectedLayer() != null) {
                getSelectedLayer().setOpacity($(".options select[name=opacity]").val());
                jCollage.redraw();
            }
        });

        $(".options select[name=blending]").change(function () {
            if (getSelectedLayer() != null) {
                getSelectedLayer().setCompositeOperation($(".options select[name=blending]").val());
                jCollage.redraw();
            }
        });

        $(".remove").click(function () {
            if (getSelectedLayer() != null) {
                jCollage.removeLayer($(".camadas .selected").attr("id").substr(6));
                updateLayers(jCollage.getLayers());
            }
        });

        $(".up").click(function () {
            if (getSelectedLayer() != null) {
                var selectedLayer = $(".camadas .selected").attr("id").substr(6);
                if (jCollage.moveLayerUp(selectedLayer)) {
                    updateLayers(jCollage.getLayers());
                    $("#layer_" + (parseInt(selectedLayer) + 1)).addClass("selected");
                }
            }
        });

        $(".down").click(function () {
            if (getSelectedLayer() != null) {
                var selectedLayer = $(".camadas .selected").attr("id").substr(6);
                if (jCollage.moveLayerDown(selectedLayer)) {
                    updateLayers(jCollage.getLayers());
                    $("#layer_" + (parseInt(selectedLayer) - 1)).addClass("selected");
                }
            }
        });

        $(window).resize(function () {
            jCollage.setOffset($("#collage").offset());
        });

        $("#gerarColagem").one('click', enviaColagem);

        $("#imageLoader").on('change', carregarImagem);

        $("#carregarArquivo").click(function() {
            $("#imageLoader").trigger('click');
        });

        $("#comboAlbuns").change(function () {
            if (debug) alert("Mudou album");
            if (debug) alert("id do album: " + this.value);
            $("#albumId").val(this.value);
            var carousel = jQuery('#mycarousel').data('jcarousel');
            $("#comboAlbuns").prop("disabled", true); // desabilita combo de albuns
            $("#sfcb_footer").hide(); // Esconde footer normal
            $("#loading_footer").show(); // Exibe footer de carregamento
            if (carousel.size() == 0) {
                if (debug) alert("Resetando carousel com 0 elementos");
                mycarousel_itemLoadCallback(carousel, "init");
            } else {
                carousel.reset();
            }
        });
        
        $(".tituloLogado").click(function(e) {
        	var resposta = confirm("Essa opção descartará a colagem. " +
        			"Deseja realmente navegar para fora da página?");
        	if (resposta != true) {
        		e.preventDefault();
        	}
        });

    });


///////////////////////////////////////////////////


    function mycarousel_itemLoadCallback(carousel, state) {

        if (state == "init") {
            if (debug) alert("itemLoadCallback inicial");
            qtdFotos = -1;
            qtdFotosCarregadas = 0;
        }

        if (qtdFotos != -1 && qtdFotosCarregadas >= qtdFotos) {
            if (debug) alert("Chamou " + state + " com todos os items carregados");
            return;
        }

        if (carousel.has(carousel.first, carousel.last)) {
            if (debug) alert("Intervalo [" + carousel.first + ", " + carousel.last + "] Já foi carregado");
            return;
        }

        if (state == "prev") {
            if (debug) alert("Tentou chamar previous");
            return;
        }

        mycarousel_makeRequest(carousel, carousel.first, carousel.last, state);

    }

    function mycarousel_makeRequest(carousel, first, last, state) {

        carousel.lock();

        var albumId = $("#albumId").val();
        var urlRequest = "";

        if (debug) alert("albumId hidden: " + albumId);
        if (debug) alert("state: " + state);

        if (state == "init") {
            if (!(typeof albumId === "undefined") && albumId != 0) {
                urlRequest = '/_ah/api/sfcb/v1/album/' + albumId + '?limit=25';
            } else {
                urlRequest = '/_ah/api/sfcb/v1/foto?limit=25';
            }

        } else if (state == "next") {
            urlRequest = '/_ah/api/sfcb/v1/foto/cursor?pagina=' + encodeURIComponent(proximaPagina);
        }

        if (debug) alert("entrou " + " first: " + first + " last: " + last + " state: " + state);
        if (debug) alert("url:" + urlRequest);

        ajaxCall('GET', urlRequest,
            function (data) {
                mycarousel_itemAddCallback(carousel, state, data, albumId);
            },
            'Erro ao obter lista de fotos');
    }

    function mycarousel_itemAddCallback(carousel, state, data, albumId) {

        if (debug) alert("state itemAddCallback: " + state);

        var chamadaPossuiFotos = !(typeof data.fotos === 'undefined');

        if (!chamadaPossuiFotos) {
            if (debug) alert("Request não trouxe fotos");
            carousel.size(qtdFotosCarregadas);
            $("#comboAlbuns").prop("disabled", false); // habilita combo de albuns
            $("#loading_footer").hide(); // Esconde footer temporario
            $("#sfcb_footer").show(); //  Exibe footer normal
            return;
        }

        if (state == "init") {
            var photoCount = $('option[value="' + albumId + '"]').data('photo-count');
            if (typeof photoCount === 'undefined') {
                photoCount = 9999;
            }
            carousel.size(photoCount);
            qtdFotos = photoCount;
            qtdFotosCarregadas = 0;
            $("#comboAlbuns").prop("disabled", false); // habilita combo de albuns
            $("#loading_footer").hide(); // Esconde footer temporario
            $("#sfcb_footer").show(); //  Exibe footer normal
            if (debug) alert("Inicilializando carousel com: " + data.fotos.length + " fotos de um total de " + qtdFotos);
        }

        $.each(data.fotos, function (i, foto) {
            carousel.add(qtdFotosCarregadas++, mycarousel_getItemHTML(foto));
        });

        carousel.unlock();

        if (typeof data.proximaPagina === 'undefined') {
            carousel.size(qtdFotosCarregadas);
            qtdFotos = qtdFotosCarregadas;
            // Quando a primeira página eh a ultima
            if (state == "init") {
                // Calcula indices visiveis
                var pos = carousel.pos(0, true);
                if (debug) alert("[" + carousel.first + ", " + carousel.last + "]" + " => " + pos);
                if (carousel.last >= qtdFotosCarregadas) {
                    // Se todas as fotos carregas estao visiveis
                    carousel.next();
                }
            }
            if (debug) alert("Ultima pagina, carregou: " + carousel.size())
        }

        proximaPagina = data.proximaPagina;

        if (debug) alert("Número de items carregados: " + qtdFotosCarregadas)

    }

    /**
     * Global item html creation helper.
     */
    function mycarousel_getItemHTML(foto) {
        var img = $("<img/>").attr("src", foto.picture);
        img.attr("title", "Camada ");
        img.data("proxy-url", foto.proxyURL);

        return img;
    }

///////////////////////////////////////////////

    function getSelectedLayer() {
        var camadas = $(".camadas .selected");
        if (camadas.length == 0) {
            return null;
        }
        return jCollage.getLayer(camadas.attr("id").substr(6));
    }

    function setSettings(id) {
        var layer = jCollage.getLayer(id);
        $(".options select[name=blending]").removeAttr("selected");
        $(".options select[name=blending] option[value=" + layer.getCompositeOperation() + "]").attr("selected", "selected");

        $(".options select[name=opacity]").removeAttr("selected");
        $(".options select[name=opacity] option[value=" + layer.getOpacity() + "]").attr("selected", "selected");

        $(".options select[name=shadow]").removeAttr("selected");
        $(".options select[name=shadow] option[value=" + layer.hasShadow() + "]").attr("selected", "selected");
    }

    function updateLayers(layers) {
        $(".camadas li.layer").remove();
        var countCamadas = 0;
        for (var i in layers) {
            //noinspection JSUnfilteredForInLoop
            $(".camadas > ul").prepend(createLayerRow(i, layers[i], ++countCamadas));
        }
    }

    function createLayerRow(id, layer, count) {
        var row = $("<li></li>").addClass("layer").attr("id", "layer_" + id);
        var icon = $("<img/>").attr("src", layer.getImage().src);
        var heading = $("<h3></h3>").text(function () {
            return layer.getTitle() + (count);
        });
        var visible = $("<div></div>").addClass("visible");
        if (layer.isVisible()) {
            visible.html("&radic;");
        }

        row.append(icon).append(heading).append(visible);

        return row;
    }

})();