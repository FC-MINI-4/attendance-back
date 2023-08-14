package com.toy4.domain.dayOffHistory.repository.info;

import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy4.domain.dayOffHistory.domain.QDayOffHistory;
import com.toy4.domain.employee.domain.QEmployee;
import com.toy4.domain.employee.dto.response.EmployeeDayOffInfoResponse;
import com.toy4.domain.schedule.RequestStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class DayOffHistoryCustomRepositoryImpl implements DayOffHistoryCustomRepository {
	private final EntityManager entityManager;

	@Override
	public List<EmployeeDayOffInfoResponse> getEmployeeDayOff() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

		QEmployee employee = QEmployee.employee;
		QDayOffHistory dayOffHistory = QDayOffHistory.dayOffHistory;

		NumberExpression<Float> dayOffUsedExpression =
			Expressions.numberTemplate(Float.class,
				"COALESCE(SUM(CASE WHEN {0} IN ({1}, {2}) THEN {3} ELSE 0 END), 0)",
				dayOffHistory.status, RequestStatus.REQUESTED, RequestStatus.APPROVED, dayOffHistory.totalAmount);

		NumberExpression<Float> dayOffTotalExpression =
			employee.dayOffRemains.add(dayOffUsedExpression);

		return queryFactory
			.select(Projections.constructor(EmployeeDayOffInfoResponse.class,
				employee.id,
				employee.name,
				employee.department.type,
				employee.position.type,
				employee.hireDate,
				dayOffUsedExpression.coalesce(0.0F).as("dayOffUsed"),
				employee.dayOffRemains,
				dayOffTotalExpression.coalesce(employee.dayOffRemains).as("dayOffTotal")
			))
			.from(employee)
			.leftJoin(dayOffHistory).on(
				employee.id.eq(dayOffHistory.employee.id)
					.and(dayOffHistory.status.in(RequestStatus.REQUESTED, RequestStatus.APPROVED))
			)
			.groupBy(employee.id, employee.name, employee.dayOffRemains)
			.fetch();
	}
}