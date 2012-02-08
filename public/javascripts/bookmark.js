$(document).ready(function(){
$("a.bookmark").click(function(e){
	e.preventDefault(); // this will prevent the anchor tag from going the user off to the link
	var bookmarkUrl = this.href;
	var bookmarkTitle = this.title;
 
	if (window.sidebar) { // For Mozilla Firefox Bookmark
		window.sidebar.addPanel(bookmarkTitle, bookmarkUrl,"");
	} else if( window.external || document.all) { // For IE Favorite
		window.external.AddFavorite( bookmarkUrl, bookmarkTitle);
	} else if(window.opera) { // For Opera Browsers
		$("a.bookmark").attr("href",bookmarkUrl);
		$("a.bookmark").attr("title",bookmarkTitle);
		$("a.bookmark").attr("rel","sidebar");
	} else { // for other browsers which does not support
		 alert('Your browser does not support this bookmark action');
		 return false;
	}
});
});