const btnMenus = document.querySelector(".btn_menu")
 btnMenus.addEventListener("click",  () => {
    const naviTotalElement = document.querySelector(".navi_total");

    // 클래스가 존재하면 토글 (있으면 제거, 없으면 추가)
    if (naviTotalElement) {
        naviTotalElement.classList.toggle("expanded");
    }
});


 btnMenus.addEventListener("click",  (e) => {
     // 클릭된 버튼 요소
    const isExpanded = e.target.getAttribute("aria-expanded") === "true"; // 현재 aria-expanded 상태 확인

    // aria-expanded 값을 현재 상태의 반대로 설정 (기본이 false)
    e.target.setAttribute("aria-expanded", isExpanded ? "false" : "true");

    // 선택적으로 expanded 클래스 토글
    const naviTotalElement = document.querySelector(".navi_total");
    if (naviTotalElement) {
        naviTotalElement.classList.toggle("expanded");
    }

    const btnService = document.querySelector(".btn_member");
    if (btnService) {
        btnService.setAttribute("aria-expanded", "false");
        btnService.classList.remove("expanded");
    }
});

document.querySelector(".btn_member").addEventListener("click", (e) => {
     // 클릭된 버튼 요소
    const isExpanded = e.target.getAttribute("aria-expanded") === "true"; // 현재 aria-expanded 상태 확인

    // aria-expanded 값을 현재 상태의 반대로 설정
    e.target.setAttribute("aria-expanded", isExpanded ? "false" : "true");

    // 클래스 토글: expanded 클래스 추가/제거
    e.target.classList.toggle("expanded");

    // .btn_menu의 aria-expanded 값을 true로 설정
    if (btnMenus) {
        btnMenus.setAttribute("aria-expanded", "false");
    }

    // .navi_total에서 expanded 클래스 제거
    const naviTotalElement = document.querySelector(".navi_total");
    if (naviTotalElement) {
        naviTotalElement.classList.remove("expanded");
    }
});

// 스크롤바 내리면 fixed 추가
// 스크롤 이벤트 리스너 추가
window.addEventListener("scroll",  ()=> {
    const body = document.getElementById("main");
    const header = document.getElementById("header");

    // 스크롤 위치가 50px 이상인지 확인
    if (window.scrollY > 50) {
        body.classList.add("fixed");
        header.classList.add("fixed");
    } else {
        body.classList.remove("fixed");
        header.classList.remove("fixed");
    }
});
