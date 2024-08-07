package syim.reviewboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import syim.reviewboard.config.auth.PrincipalDetail;
import syim.reviewboard.model.Board;
import syim.reviewboard.model.User;
import syim.reviewboard.service.BoardService;
import org.springframework.ui.Model;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    //게시글 목록 출력 - model 활용
    @GetMapping({"", "/"})
    public String index(Model model, @PageableDefault(size = 7, sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        model.addAttribute("boards", boardService.getPostList(pageable));
        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String getPost(Model model, @PathVariable int id) {
        model.addAttribute("boards", boardService.getPost(id));
        return "board/detail";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(Model model, @PathVariable int id) {
        model.addAttribute("boards", boardService.getPost(id));
        return "board/updateForm";
    }

    //로그인한 사용자 본인의 게시글 조회
    @GetMapping("/board/userPosts")
    public String getUserPosts(@AuthenticationPrincipal PrincipalDetail principalDetail, Model model, Pageable pageable) {
        User user = principalDetail.getUser();
        Page<Board> userPosts = boardService.getUserPosts(user, pageable);
        model.addAttribute("boards", userPosts);
        return "board/userPosts";
    }
}
