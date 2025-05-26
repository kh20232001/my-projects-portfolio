	
/**
 * 
 */

 function displayCount(now){
     document.getElementById("postCount").value = document.getElementById("displayCount").value;
     const nows = parseInt(document.getElementById("nows").innerText);
     
     if(now == 'Previous'){
        place = nows - 1;
     }
     else if(now == 'Next'){
        place = nows + 1; 
     }
     else if(now == 'after'){
        place = nows + 1; 
     }
     else if(now == 'afterSecond'){
        place = nows + 2; 
     }
     document.getElementById("nowCount").value = place;
     document.getElementById("paginationForm").submit();
 }
