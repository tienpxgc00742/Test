<%-- 
    Document   : toast
    Created on : Sep 5, 2015, 4:43:58 PM
    Author     : MrTien
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<button type="button" class="btn btn-success" id="showtoast">Show Toast</button>
<script type="text/javascript">
    $(function () {
        var i = -1;
        var toastCount = 0;
        var $toastlast;
        var getMessage = function () {
            var msgs = ['Hi, this is toastr notification',
                '<div><input class="input-small" value="textbox"/>&nbsp;<a href="http://thevectorlab.net/flatlab/" target="_blank">This is a hyperlink</a></div><div><button type="button" id="okBtn" class="btn btn-primary">Close me</button><button type="button" id="surpriseBtn" class="btn" style="margin: 0 8px 0 8px">Surprise me</button></div>',
                'Flatlab is awesome',
                'Build with BS3!',
                'Easy to use',
                'Have fun storming the castle!'
            ];
            i++;
            if (i === msgs.length) {
                i = 0;
            }

            return msgs[i];
        };
        $('#showtoast').click(function () {
            toastr.options = {
                closeButton: true,
                debug: false,
                progressBar: true,
                positionClass: "toast-top-right",
                onclick: null,
                showDuration: "300",
                hideDuration: "1000",
                timeOut: "5000",
                extendedTimeOut: "1000",
                showEasing: "swing",
                hideEasing: "linear",
                showMethod: "fadeIn",
                hideMethod: "fadeOut"
            };
            toastr.success("Flatlab is an Awesome dashboard build with BS3 ", "Toastr Notification");
        });
        function getLastToast() {
            return $toastlast;
        }
        $('#clearlasttoast').click(function () {
            toastr.clear(getLastToast());
        });
        $('#cleartoasts').click(function () {
            toastr.clear();
        });
    })
</script>