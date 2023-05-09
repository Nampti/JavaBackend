package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.OrderListService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

@Controller
public class BurgerController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderListService orderListService;
	
	@RequestMapping(value = "/admin/dashboard", method = RequestMethod.GET)
	public String dashboard(Model model) {
		model.addAttribute("totalProduct", productService.findAll().size());
		model.addAttribute("totalCategory", categoryService.findAll().size());
		model.addAttribute("totalCustomers", userService.getListCustomers().size());
		model.addAttribute("totalAdmins", userService.getListAdmins().size());
		model.addAttribute("totalOrders", orderListService.findAllOrders().size());
		model.addAttribute("earningmoney", orderListService.earnmoneythismonth());
		
		List<Product> list = orderListService.findProductsByOrder_id(1);
		
		for (Product product : list) {
			System.out.println(new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getQuantity()));
		}
		return "/admin/dashboard";
	}
	@RequestMapping(value = "/admin/products", method = RequestMethod.GET)
	public String getListProducts(Model model) {
		model.addAttribute("listProducts", productService.findAll());
		return "/admin/listProducts";
	}
	
	@RequestMapping(value = "/admin/products/search", method = RequestMethod.GET)
	public String searchProducts(@Param("name") String keyword, Model model) {
		model.addAttribute("keyword", keyword);
		model.addAttribute("listFounds", productService.findByKeyword(keyword));
		return "/admin/listFoundProducts";
	}
	
	@RequestMapping(value = "/admin/newProduct", method = RequestMethod.GET)
	public String showNewProductForm(Model model) {
		model.addAttribute("newProduct", new Product());
		model.addAttribute("listCategories", categoryService.findAll());
		return "/admin/newProductForm";
	}
	
	@RequestMapping(value = "/admin/saveProduct", method = RequestMethod.POST)
	public String saveProduct(@RequestParam("name") String name,
			@RequestParam("description") String description,
			@RequestParam("quantity") int quantity,
			@RequestParam("price") int price,
			@RequestParam("category") Category category,
			@RequestParam("image") MultipartFile file, Model model) {
		if(productService.isDuplicatedName(name)) {
			model.addAttribute("failed", "Ten san pham da ton tai");
			model.addAttribute("newProduct", new Product());
			model.addAttribute("listCategories", categoryService.findAll());
			return "admin/newProductForm";
		}
		productService.saveProduct(name, description, quantity, price, category, file);
		System.out.println(file.getOriginalFilename());
		return "redirect:/admin/products";
	}
	
	@RequestMapping(value = "/admin/update/{id}")
	public String showUpdateProductForm(@PathVariable("id") int id, Model model) {
		Product product = productRepository.findById(id).orElse(null);
		System.out.println(product);
		model.addAttribute("foundProduct", product);
		model.addAttribute("listCategories", categoryService.findAll());
		model.addAttribute("imageBase64", product.getImage());
		return "/admin/updateProductForm";
	}
	
	
	@RequestMapping(value = "/admin/updateProduct", method = RequestMethod.POST)
	public String updateProduct(@RequestParam("id") Integer id,
			@RequestParam("name") String name,
			@RequestParam("description") String description,
			@RequestParam("quantity") int quantity,
			@RequestParam("price") int price,
			@RequestParam("category") Category category,
			@RequestParam("image") MultipartFile file, Model model) {
		productService.updateProduct(id,name, description, quantity, price, category, file);
		
		return "redirect:/admin/products";
	}
	
	@RequestMapping(value = "/admin/delete/{id}")
	public String deleteProduct(@PathVariable("id") int id) {
		productService.deleteProduct(id);
		return "redirect:/admin/products";
	}
	
	// category
	@RequestMapping(value = "/admin/categories", method = RequestMethod.GET)
	public String getListCategories(Model model) {
		List<CategoryDTO> list = categoryService.findAll();
		model.addAttribute("listCategories", list);
		return "/admin/listCategories";
	}
	
	@RequestMapping(value = "/admin/categories/search", method = RequestMethod.GET)
	public String searchCategories(@Param("name") String keyword, Model model) {
		model.addAttribute("keyword", keyword);
		model.addAttribute("listFounds", categoryService.findByKeyword(keyword));
		return "/admin/listFoundCategories";
	}
	
	
	@RequestMapping(value = "/admin/newCategory", method = RequestMethod.GET)
	public String showNewCategoryForm(Model model) {
		model.addAttribute("newCategory", new CategoryDTO());
		return "/admin/newCategoryForm";
	}
	
	@RequestMapping(value = "/admin/saveCategory", method = RequestMethod.POST)
	public String saveCategory(@ModelAttribute("newCategory") CategoryDTO categoryDTO, Model model) {
		if(categoryService.isDuplicatedName(categoryDTO.getName())) {
			model.addAttribute("failed","Ten da ton tai");
			model.addAttribute("newCategory", new CategoryDTO());
			return "admin/newCategoryForm";
		}
		else {
			categoryService.save(categoryService.convertFromCategoryDTO(categoryDTO));
			return "redirect:/admin/categories";
		}
	}
	
	@RequestMapping(value = "/admin/category/update/{id}")
	public String showUpdateCategoryForm(@PathVariable("id") int id, Model model) {
		CategoryDTO categoryDTO = categoryService.getById(id);
		model.addAttribute("foundCategory", categoryDTO);
		return "/admin/updateCategoryForm";
	}
	
	@RequestMapping(value = "/admin/updateCategory", method = RequestMethod.POST)
	public String updateCategory(@ModelAttribute("foundCategory") CategoryDTO categoryDTO) {
		categoryService.save(categoryService.update(categoryDTO));
		return "redirect:/admin/categories";
	}
	
	@RequestMapping(value = "/admin/category/delete/{id}")
	public String deleteCategory(@PathVariable("id") int id) {
		categoryService.deleteById(id);
		return "redirect:/admin/categories";
	}
	@RequestMapping(value = "/admin/ordersList")
	public String getOrderList(Model model) {
		List<Order> orders = orderListService.findAllOrders();
		System.out.println(orders);
		model.addAttribute("listOrders",orders);
		return "admin/listOrders";
	}
	
	@RequestMapping(value = "/admin/orders/{id}")
	public String getOrderDetailsById(@PathVariable("id") int id ,Model model) {
		Order order = orderListService.findOrderById(id);
		List<ProductDTO> productDTOs = new ArrayList<>();
		for (Product product : orderListService.findProductsByOrder_id(id)) {
			ProductDTO productDTO = new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
			productDTOs.add(productDTO);
		}
		model.addAttribute("order", order);
		model.addAttribute("products", productDTOs);
		return "admin/orderDetail";
	}
	// users
		// customers
		@RequestMapping(value = "/admin/customers")
		public String getListCustomers(Model model) {
			List<User> customers = userService.getListCustomers();
			model.addAttribute("listCustomers", customers);
			return "admin/listCustomers";
		}

}
