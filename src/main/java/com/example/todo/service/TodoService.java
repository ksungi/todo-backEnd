package com.example.todo.service;

import java.io.Console;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.model.TodoEntity;
import com.example.todo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {
	
	@Autowired
	private TodoRepository repository;
	
	//CREATE
	public List<TodoEntity> create(final TodoEntity entity){
		//Validations
		validate(entity);
		repository.save(entity);
		
		return repository.findByUserId(entity.getUserId()); 
	}
	
	
	public List<TodoEntity> retrieve(final String userId) {
		return repository.findByUserId(userId);
	}
	
	//UPDATE
	public List<TodoEntity> update(final TodoEntity entity) {
		//Validations
		validate(entity);
		if (repository.existsById(entity.getId())) {
			repository.save(entity);
		}else
			throw new RuntimeException("Unknown id");
		
		return repository.findByUserId(entity.getUserId());
	}
	
	//UPDATE-TODO 사용하지 않음
//	public Optional<TodoEntity> updateTodo(final TodoEntity entity) {
//		//Validations
//		//validate(entity); //이녀석이 원인
//		
//		//테이블에서 id에 해당하는 데이터 셋을 가져온다.
//		final Optional<TodoEntity> original = repository.findById(entity.getId());
//		
//		//original에 담긴 내용을 todo에 할당하고 title, done 값을 변경
//		original.ifPresent(todo -> {
//			todo.setTitle(entity.getTitle());
//			todo.setDone(entity.isDone());
//			repository.save(todo);
//		});
//		//위 람다식과 같은 표현
//		/** if(original.isPresent()) {
//		*	 final TodoEntity todo = original.get();
//		*	 todo.setTitle(entity.getTitle());
//		*	 todo.setDone(entity.isDone());
//		*	 repository.save(todo);
//		 }*/
//		
//		return repository.findById(entity.getId());
//	}
	
	//DELETE
	public List<TodoEntity> delete(final TodoEntity entity) {
		if( repository.existsById( entity.getId() ) )
			repository.deleteById( entity.getId() );
		else
			throw new RuntimeException("id does not exist");
		
		return repository.findByUserId(entity.getUserId());
	}
	
	//Validation
	public void validate(final TodoEntity entity) {
		if(entity == null) {
			log.warn("Entity can't be null");
			throw new RuntimeException("Entity can't be null");
			
		}
		if(entity.getUserId() == null) {
			log.warn("Unknown user");
			throw new RuntimeException("Unknown user");
		}
	}

}
