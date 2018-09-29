/**   
 * Copyright © 2018 北京易酒批电子商务有限公司. All rights reserved.
 */
package com.oreo.himalaya;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Title: Test.java
 * @Package com.oreo.himalaya
 * @Description:
 * @author wangran
 * @date 2018年9月29日 下午2:42:07
 * @version V1.0
 */
@SpringBootTest(classes = SpringBoot2Application.class)
@RunWith(SpringRunner.class)
public class ReactorTest {
	
	@Test
	public void fluxTest() {
		
		Flux.generate(sink -> {
		    sink.next("Hello");
		    sink.complete();
		}).subscribe(System.out::println);
		
		final Random random = new Random();
		List<Integer> finalList = new ArrayList<>();
		Flux.generate(ArrayList :: new, (list, sink) -> {
			Integer value = random.nextInt();
			list.add(value);
			sink.next(value);
			if (list.size() == 10) {
				sink.complete();
			}
			return list;
		}).subscribe((item) -> finalList.add((Integer)item));
		
		finalList.stream().forEach(System.err :: println);
		
		Mono.justOrEmpty(Optional.of("Hello Mono")).subscribe(System.out::println);
		
		Flux.range(1, 100).buffer(20).subscribe(System.out::println);
//		Flux.intervalMillis(100).bufferMillis(1001).take(2).toStream().forEach(System.out::println);
		Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
		Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);
		
		
		CountDownLatch latch = new CountDownLatch(1);
		
		Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
			List<Integer> list = new ArrayList<>();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0 ; i< 10 ;i++) {
				list.add(i);
			}
			return list;
		}).whenComplete((list, excetion) -> {
			for (int i = 10 ; i< 20 ;i++) {
				list.add(i);
			}
		})).block();

		Flux.just(1, 2)
        .concatWith(Mono.error(new IllegalStateException()))
        .subscribe(System.out::println, System.err::println);
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
//		CompletableFuture<String> future = CompletableFuture.runAsync(runnable)
//		Mono.fromFuture(future).
		
	}
	
	private List<Integer> getList() {
		CompletableFuture<List<Integer>> strListFutrue = CompletableFuture.supplyAsync(() -> {
			List<Integer> list = new ArrayList<>();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0 ; i< 10 ;i++) {
				list.add(i);
			}
			return list;
		}).whenComplete((list, excetion) -> {
			for (int i = 10 ; i< 20 ;i++) {
				list.add(i);
			}
		});
		try {
			return strListFutrue.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new ArrayList<Integer>();
	}
	
}
