(function () {
    var jCollage = null;
    var proximaPagina;
    var paginaAnterior;
    var qtdFotos = -1;
    var qtdFotosCarregadas = 0;

    function setHeader(xhr) {
        xhr.setRequestHeader('access-token', $("#access-token").val());
    }

    $(document).ready(function () {
        jCollage = new Collage("#collage");
        jCollage.setBackgroundColor("#fff");

        $(document).on("click", ".fotos img", function () {
            var img = $(this);
            var imgUrl = document.createElement("img");
            imgUrl.onload = function () {
                jCollage.addLayer(imgUrl).setTitle(img.attr("title"));
                updateLayers(jCollage.getLayers());
                $("#layer_" + (jCollage.getLayers().length - 1)).addClass("selected");
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


        $("#recuperarFotos").click(function () {
            $.ajax({
                url: location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '')
                    + '/_ah/api/sfcb/v1/foto',
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                    proximaPagina = data.proximaPagina;
                    paginaAnterior = data.paginaAnterior;
                    $.each(data.fotos, function (i, foto) {
                        var img = $("<img/>").attr("src", foto.picture);
                        img.attr("title", "Camada ");
                        img.data("proxy-url", foto.proxyURL);
                        //      $("<li></li>").append(img).appendTo(".fotos ul");
                        jQuery('#mycarousel').jcarousel('add', i, img);
                    });
                },
                error: function () {
                    alert('Erro ao obter lista de fotos');
                },
                beforeSend: setHeader
            });

        });

        $("#gerarColagem").click(function () {
            var canvas = document.getElementById('collage');
            var dataURL = canvas.toDataURL('image/jpeg');

            $("#dataColagem").val(dataURL);
            $("#formColagem").submit();
        });

    });


///////////////////////////////////////////////////


    function mycarousel_itemLoadCallback(carousel, state) {

        if (qtdFotos != -1 && qtdFotosCarregadas >= qtdFotos) {
            return;
        }

        if (state == "prev") {
            alert("Tentou chamar previous");
            return;
        }

        mycarousel_makeRequest(carousel, carousel.first, carousel.last, state);

    };

    function mycarousel_makeRequest(carousel, first, last, state) {

        carousel.lock();

        var urlRequest = "";

        if (state == "init") {
            urlRequest = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '')
                + '/_ah/api/sfcb/v1/foto?limit=25';
        } else if (state == "next") {
            urlRequest = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '')
                + '/_ah/api/sfcb/v1/foto/cursor?pagina=' + encodeURIComponent(proximaPagina);
        }

        alert("entrou " + " first: " + first + " last: " + last + " state: " + state);
        alert("url:" + urlRequest);

        $.ajax({
            url: urlRequest,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                mycarousel_itemAddCallback(carousel, first, last, state, data);
            },
            error: function () {
                alert('Erro ao obter lista de fotos');
            },
            beforeSend: setHeader
        });

    };

    function mycarousel_itemAddCallback(carousel, first, last, state, data) {

        if (state == "init") {
            carousel.size(data.count);
            qtdFotos = data.count;
            $(".fotos").toggle(); // Esconde footer temporario e habilita footer final
            alert("Inicilializando carousel com: " + data.fotos.length + " fotos de um total de " + qtdFotos);
        }

        $.each(data.fotos, function (i, foto) {
            carousel.add(qtdFotosCarregadas++, mycarousel_getItemHTML(foto));
        });

        proximaPagina = data.proximaPagina;
        if (typeof proximaPagina === 'undefined') {
            carousel.size(qtdFotosCarregadas > 0 ? qtdFotosCarregadas++ : 0);
            qtdFotos = qtdFotosCarregadas;
            alert("Ultima pagina, carregou: " + qtdFotosCarregadas)
        }
        carousel.unlock();

        alert("Número de items carregados: " + qtdFotosCarregadas)

    };

    /**
     * Global item html creation helper.
     */
    function mycarousel_getItemHTML(foto) {
        var img = $("<img/>").attr("src", foto.picture);
        img.attr("title", "Camada ");
        img.data("proxy-url", foto.proxyURL);

        return img;
    };

    jQuery(document).ready(function () {
        jQuery('#mycarousel').jcarousel({
            itemLoadCallback: mycarousel_itemLoadCallback
        });
    });


///////////////////////////////////////////////

    function getSelectedLayer() {
        if ($(".camadas .selected").length == 0) {
            return null;
        }
        return jCollage.getLayer($(".camadas .selected").attr("id").substr(6));
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
        for (i in layers) {
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