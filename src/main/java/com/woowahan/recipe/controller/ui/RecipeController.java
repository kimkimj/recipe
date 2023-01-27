package com.woowahan.recipe.controller.ui;

import com.woowahan.recipe.domain.dto.recipeDto.RecipeCreateReqDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeFindResDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipePageResDto;
import com.woowahan.recipe.domain.dto.recipeDto.RecipeUpdateReqDto;
import com.woowahan.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recipes")
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("recipeCreateReqDto", new RecipeCreateReqDto());
        return "recipe/createForm";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute RecipeCreateReqDto form, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return "recipe/createForm";
        }
        System.out.println(form.getRecipeBody() + form.getRecipeTitle());
        String userName = "messi"; // 인증 생기기 전까지 임시 사용
//        String userName = authentication.getName();
        recipeService.createRecipe(form, userName);
        return "redirect:/recipes/list";
    }

    @GetMapping("/update/{recipeId}")
    public String updateForm(Model model,@PathVariable Long recipeId) {
        model.addAttribute("recipeUpdateReqDto", new RecipeUpdateReqDto());
        model.addAttribute("recipeId", recipeId);
        return "recipe/updateForm";
    }

    @PostMapping("/update/{recipeId}")
    public String update(@Valid @ModelAttribute RecipeUpdateReqDto form, BindingResult result, @PathVariable Long recipeId, Authentication authentication) {
        if (result.hasErrors()) {
            return "recipe/updateForm";
        }
        System.out.println("1");
        String userName = "messi"; // 인증 생기기 전까지 임시 사용
//        String userName = authentication.getName();
        recipeService.updateRecipe(form, recipeId, userName);
        System.out.println("2");
        return "redirect:/recipes/list";
    }

    @GetMapping("/{recipeId}")
    public String findRecipe(@PathVariable Long recipeId, Model model) {
        recipeService.updateView(recipeId);
        RecipeFindResDto recipe = recipeService.findRecipe(recipeId);
        model.addAttribute("recipeId", recipeId);
        model.addAttribute("recipe", recipe);
        return "recipe/recipeDetailList";
    }

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RecipePageResDto> allRecipes = recipeService.findAllRecipes(pageable);
        model.addAttribute("allRecipes", allRecipes);
        return "recipe/recipeList";
    }

    @GetMapping("/my")
    public String myRecipes(Authentication authentication, Model model, @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        String userName = "messi"; // 인증 생기기 전까지 임시 사용
//        String userName = authentication.getName();
        Page<RecipePageResDto> myRecipes = recipeService.myRecipes(pageable, userName);
        model.addAttribute("myRecipes", myRecipes);
        return "recipe/myRecipes";
    }
}