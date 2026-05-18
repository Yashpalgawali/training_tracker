package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.TestingScheduleDto;
import com.example.demo.entity.Test;
import com.example.demo.entity.TestSchedule;
import com.example.demo.entity.TestScheduleHistory;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.mapper.TestingScheduleMapper;
import com.example.demo.repository.TestingScheduleRepository;
import com.example.demo.service.ITestScheduleService;
import com.example.demo.service.ITestService;

import lombok.RequiredArgsConstructor;

@Service("testscheduleserv")
@RequiredArgsConstructor
public class TestingScheduleServImpl implements ITestScheduleService {

	private final TestingScheduleRepository testshedulerepo;

	private final TestScheduleHistRepo testschedulehistrepo;
	
	private final ITestService testserv;
	
	@Override
	public void saveTestSchedule(TestingScheduleDto testScheduleDto) {
		var testScheduleDtoObj = testserv.getTestById(testScheduleDto.getTestId());
		
		TestSchedule mappedTestingSchedule = TestingScheduleMapper.MapToTestingSchedule(testScheduleDto,
				new TestSchedule());
		
		String status = testScheduleDto.getStatus();

		if(status.equals("PLAN")) {
			mappedTestingSchedule.setPlan(status);
			mappedTestingSchedule.setDone("");
		}
		if(status.equals("DONE")) {
			mappedTestingSchedule.setDone(status);
			mappedTestingSchedule.setPlan("");
		}
		
		String year = testScheduleDto.getTestScheduleDate().substring(8, 10);
		Integer in = testScheduleDto.getMonthIndex().intValue();

		switch (in) {
		// Case statements
		case 1:
			mappedTestingSchedule.setMonthJan("Jan" + year);
			break;
		case 2:
			mappedTestingSchedule.setMonthFeb("Feb" + year);
			break;
		case 3:
			mappedTestingSchedule.setMonthMar("Mar" + year);
			break;
		case 4:
			mappedTestingSchedule.setMonthApr("Apr" + year);
			break;
		case 5:
			mappedTestingSchedule.setMonthMay("May" + year);
			break;
		case 6:
			mappedTestingSchedule.setMonthJun("Jun" + year);
			break;
		case 7:
			mappedTestingSchedule.setMonthJul("Jul" + year);
			break;
		case 8:
			mappedTestingSchedule.setMonthAug("Aug" + year);
			break;
		case 9:
			mappedTestingSchedule.setMonthSep("Sep" + year);
			break;
		case 10:
			mappedTestingSchedule.setMonthOct("Oct" + year);
			break;
		case 11:
			mappedTestingSchedule.setMonthNov("Nov" + year);
			break;
		case 12:
			mappedTestingSchedule.setMonthDec("Dec" + year);
			break;
		// Default case statement
		default:
			mappedTestingSchedule.setMonthDec("Dec" + year);
		}

		Test comm = testserv.getTestById(testScheduleDto.getTestId());
		mappedTestingSchedule.setTest(comm);
		
		TestSchedule savedEntity = testshedulerepo.save(mappedTestingSchedule);

		if (savedEntity != null) {
			TestScheduleHistory histObj = new TestScheduleHistory();
			
			histObj.setTest(testScheduleDtoObj.getTestName());
			histObj.setTestScheduleDate(testScheduleDto.getTestScheduleDate());
			histObj.setApprovedBy(testScheduleDto.getApprovedBy());
			histObj.setCheckedBy(testScheduleDto.getCheckedBy());
			histObj.setDoneBy(testScheduleDto.getDoneBy());
			histObj.setFrequency(testScheduleDto.getFrequency());
			histObj.setStatus(testScheduleDto.getStatus());

			testschedulehistrepo.save(histObj);
		}

		if (savedEntity == null) {
			throw new GlobalException("Testing  is not scheduled");
		}
	}

	@Override
	public void updateTestSchedule(TestingScheduleDto testScheduleDto) {

	}

	@Override
	public TestSchedule getTestScheduleById(Long id) {

		return testshedulerepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No Testing schedule was found for given ID " + id));
	}

//	@Override
//	public List<TestingScheduleDto> getTestScheduleByYear(String year) {
//		List<TestSchedule>  scheduleList = testshedulerepo.findTestingScheduleByYear(year);
//		if(scheduleList.size() > 0 ) {
//			List<TestingScheduleDto>  scheduleListDto = scheduleList.stream().map(-> {
//				TestingScheduleDto testDto = new TestingScheduleDto();
//					testDto.setTestingScheduleId(.getTestScheduleId());
//					testDto.setTestId(.getTest().getTestingId());
//					testDto.setApprovedBy(.getApprovedBy());
//					testDto.setDoneBy(.getDoneBy());
//					testDto.setCheckedBy(.getCheckedBy());
//					testDto.setFrequency(.getFrequency());
//					if(.getDone().equalsIgnoreCase("done"))
//					{
//						testDto.setStatus(.getDone());
//					}
//					
//					if(.getPlan().equalsIgnoreCase("plan"))
//					{
//						testDto.setStatus(.getPlan());
//					}
//					testDto.setTestScheduleDate(.getTestScheduleDate());
//				return testDto;
//				
//			}).collect(Collectors.toList());
//			return scheduleListDto;
//		}		
//		
//		throw new ResourceNotFoundException("No Testing Schedule was found for given year " + year);
//	}
	
	@Override
	public List<TestSchedule> getTestScheduleByYear(String year) {
		List<TestSchedule>  scheduleList = testshedulerepo.findTestingScheduleByYear(year);
		 if(scheduleList.size() > 0 ) {
			 return scheduleList;
		 }
		
		throw new ResourceNotFoundException("No Testing Schedule was found for given year " + year);
	}

	@Override
	public List<TestSchedule> getAllTestSchedules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void updateTestScheduleSignatureByYear(Map<String , String> body,String year){
		
		List<TestSchedule> testScheduleByYear = getTestScheduleByYear(year);
		
		String doneBy = body.get("doneBy");
		String checkedBy = body.get("checkedBy");
		String approvedBy = body.get("approvedBy");
		
		TestSchedule testSchedule = new TestSchedule();
		
		testSchedule.setApprovedBy(approvedBy);
		testSchedule.setCheckedBy(checkedBy);
		testSchedule.setDoneBy(doneBy);
		
		int res = testshedulerepo.updateTestingScheduleSignatureByYear(doneBy, checkedBy, approvedBy, year);
		if(res < 0)
			throw new ResourceNotModifiedException("The signature for year "+year+" is not updated");
		
	}

	@Override
	public void deleteTestScheduleById(Long testScheduleId) {
		Optional<TestSchedule> found = testshedulerepo.findById(testScheduleId);  
		if(!found.isPresent()){
			throw new ResourceNotFoundException("No Test schedule is found!!");
		}

		TestSchedule testSchedule = found.get();
		
		TestScheduleHistory histObj = new TestScheduleHistory();
		histObj.setTest(testSchedule.getTest().getTestName());
		histObj.setTestScheduleDate(testSchedule.getTestScheduleDate());
		histObj.setApprovedBy(testSchedule.getApprovedBy());
		histObj.setCheckedBy(testSchedule.getCheckedBy());
		histObj.setDoneBy(testSchedule.getDoneBy());
		histObj.setFrequency(testSchedule.getFrequency());
		histObj.setStatus("Deleted");
		 
		testschedulehistrepo.save(histObj);
		
		testshedulerepo.deleteById(testScheduleId);
		
		Optional<TestSchedule> testById = testshedulerepo.findById(testScheduleId);
		if(testById.isPresent()) {
			throw new ResourceNotModifiedException("Test Schedule is not deleted");
		}
	}

}
