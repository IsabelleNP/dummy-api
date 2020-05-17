package com.example.dummyapi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
class UserController {

	@Autowired
	lateinit var userRepository: UserRepository

	@GetMapping("/user/{id}")
	fun getUserById(@PathVariable id: Int): ResponseEntity<UserEntity> {
		val user = userRepository.findById(id)

		if (user.isEmpty) {
			return ResponseEntity.notFound().build()
		}

		return ResponseEntity(user.get(), HttpStatus.OK)
	}

	@GetMapping("/user")
	fun getAllUsers(): ResponseEntity<List<UserEntity>> {
		return ResponseEntity(userRepository.findAll(), HttpStatus.OK)
	}

	@PostMapping("/user")
	fun saveUser(@RequestBody @Valid user: UserEntity): ResponseEntity<UserEntity> {
		userRepository.save(user)

		return ResponseEntity(HttpStatus.OK)
	}

	@PutMapping("/user/{id}")
	fun updateUser(@RequestBody @Valid user: UserEntity, @PathVariable id: Int): ResponseEntity<UserEntity> {
		val currentUser = userRepository.findById(id)

		if (currentUser.isEmpty) {
			return ResponseEntity.notFound().build()
		}

		user.id = id
		userRepository.save(user)

		return ResponseEntity(HttpStatus.OK)
	}

	@DeleteMapping("/user/{id}")
	fun deleteUser(@PathVariable id: Int): ResponseEntity<UserEntity> {
		val user = userRepository.findById(id)

		if (user.isEmpty) {
			return ResponseEntity.notFound().build()
		}

		userRepository.deleteById(id)
		return ResponseEntity(HttpStatus.OK)
	}
}

interface UserRepository: JpaRepository<UserEntity, Int>

@Entity
data class UserEntity (
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Int? = null,

	@Column
	@NotNull
	var name: String? = null,

	@Column
	@NotNull
	var email: String? = null
)

@SpringBootApplication
class DummyApiApplication

fun main(args: Array<String>) {
	runApplication<DummyApiApplication>(*args)
}