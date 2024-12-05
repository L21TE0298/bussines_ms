package com.ghostappi.backend.controller;

import java.util.List;

import com.ghostappi.backend.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
//import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import com.ghostappi.backend.model.Sale;
import com.ghostappi.backend.service.SaleService;
import com.ghostappi.backend.repository.UserRepository;


@RestController

@RequestMapping("sales")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT })
@Validated
@Tag(name = "Sale Management", description = "Provides methods for managing sales")
public class SaleController {

	@Autowired
	private SaleService saleService;

	@Autowired
	private UserRepository userRepository;

	@Operation(summary = "Get all sales")
	@ApiResponse(responseCode = "200", description = "Found Sales", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Sale.class)))})
	@GetMapping
	public List<Sale> getAll() {
		return saleService.getAll();
	}

	@Operation(summary = "Get a sale by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sale found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Sale.class))}),
			@ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Sale not found", content = @Content)})
	@GetMapping("{idSale}")
	public ResponseEntity<?> getByIdSale(@PathVariable @Min(value = 1 , message="The required request parameter idSale is negative our missing.") Integer idSale) {
		try {
			Sale sale = saleService.getByIdSale(idSale);
			return new ResponseEntity<Sale>(sale, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>("Sale not found", HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Register a sale")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Sale saved", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid sale information", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error saving record", content = @Content)})
	@PostMapping
	public ResponseEntity<?> registrar(@Valid @RequestBody Sale sale) {
		try {
			// Verificar si el usuario ya existe en la base de datos
			User user = sale.getUser();
			if (user != null && user.getIdUser() != null) {
				// Si el usuario no existe, guardarlo primero
				if (!userRepository.existsById(user.getIdUser())) {
					user = userRepository.save(user);
				}
			} else {
				return new ResponseEntity<String>("User information is missing or incomplete", HttpStatus.BAD_REQUEST);
			}
			sale.setUser(user);
			saleService.save(sale);
			return new ResponseEntity<String>("Saved record", HttpStatus.CREATED); // Cambié a 201 para un nuevo recurso
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error saving record: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Delete a sale by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sale deleted", content = @Content),
			@ApiResponse(responseCode = "404", description = "Sale not found", content = @Content)})
	@DeleteMapping("{idSale}")
	public ResponseEntity<?> delete(@PathVariable @Min(value = 1 , message="The required request parameter idSale is negative our missing.") Integer idSale) {
		try {
			saleService.deleteById(idSale);
			return new ResponseEntity<>("Sale deleted", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error deleting sale: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Get all sales with pagination")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found sales", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Sale.class))}),
			@ApiResponse(responseCode = "400", description = "Invalid page or size supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Sales not found", content = @Content)})
	@GetMapping(value = "pagination", params = {"page", "size"})
	public ResponseEntity<List<Sale>> getAllPaginated(
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int pageSize) {

		List<Sale> sales = saleService.getAll(page, pageSize);
		return new ResponseEntity<>(sales, HttpStatus.OK);
	}
}