package com.example.test_app_kai.dao;

import com.example.test_app_kai.output_data.HarvesterResult;
import com.example.test_app_kai.output_data.TrainTicket;
import com.example.test_app_kai.task_service.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.sql.*;

import static com.example.test_app_kai.utils.LazyJson.fromJson;

@Getter
@Setter
@Slf4j
@Service
@Component
@AllArgsConstructor
public class JdbcTemplateTaskDaoImpl implements TaskDao {

    private final Task task;
    private final DatabaseConnection dbConnection;

    final String driver = "org.mariadb.jdbc.Driver";

    @Override
    public Task getByHarvesterName(String name, int serverNumber) throws SQLNonTransientConnectionException {

        String procedure = "{CALL get_task_from_harvester_queue(?, ?)}";

        String jsonTask = null;

        try {
            Connection connection = dbConnection.getConnection();
            CallableStatement cs = connection.prepareCall(procedure);
            Class.forName(driver);
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.setString(1, name);
            cs.setInt(2, serverNumber);
            cs.execute();

            ResultSet resultSet = cs.getResultSet();
            while (resultSet.next()) {
                jsonTask = resultSet.getString(1);
                log.info("Rib return task: {}", jsonTask);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return fromJson(jsonTask, Task.class);
    }

    public void sendHarvestResultMessage(String taskId, String harvestResultMessage, int harvestResultCode) {
        String procedure = "{CALL give_the_answer_for_harvester_queue(?, ?, ?)}";
        boolean sendResultSuccess = false;
        do {
            try {
                Connection connection = dbConnection.getConnection();
                CallableStatement cs = connection.prepareCall(procedure);

                Class.forName(driver);
                cs.registerOutParameter(1, Types.VARCHAR);
                cs.setString(1, taskId);
                cs.setString(2, harvestResultMessage);
                cs.setInt(3, harvestResultCode);
                cs.execute();
                sendResultSuccess = true;
                log.info("Result sended.");
//                break;
            } catch (ClassNotFoundException e) {
                log.error("ClassNotFoundException in give task: {}", String.valueOf(e));
            } catch (SQLInvalidAuthorizationSpecException e) {
                log.error("SQLInvalidAuthorizationSpecException in give task: {}", String.valueOf(e));
            } catch (SQLNonTransientConnectionException e) {
                log.error("SQLNonTransientConnectionException in give task: {}", String.valueOf(e));
            } catch (SQLTransactionRollbackException e) {
                log.error("SQLTransactionRollbackException in give task: {}", String.valueOf(e));
            } catch (SQLException e) {
                log.error("SQLException in give task: {}", String.valueOf(e));
            }
        } while (!sendResultSuccess);
    }

    @Override
    public String sendShortDataInfo(HarvesterResult harvestResult, String jsonConfig) throws SQLException {
        String md5Checksumm = DigestUtils.md5Hex(harvestResult.getDateTimestamp() + harvestResult.getTickets().size() + harvestResult.getResultFileName());
        insertData("INSERT INTO tcs__slice_list (file_md5checksum, file_date, file_size, file_name, json_configuration) VALUES ('" + md5Checksumm + "', " + harvestResult.getDateTimestamp() + ", " + harvestResult.getTickets().size() + ", '" + harvestResult.getResultFileName() + "', '" + jsonConfig + "')");
        return md5Checksumm;
    }

    @Override
    public boolean getShortDataStatus(String md5) {
        String procedure = "{ CALL get_slice_status(?) }";
        try {
            Connection connection = dbConnection.getConnection();
            CallableStatement cs = connection.prepareCall(procedure);

            Class.forName(driver);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(1, md5);
            cs.execute();
            ResultSet resultSet = cs.getResultSet();
            while (resultSet.next()) {
                int result = resultSet.getInt(1);
                return result == 1;
            }
        } catch (SQLException e) {
            log.error("SQLException in check slice status: {}", String.valueOf(e));
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException in get task: " + e);
        }
        log.info("Slice doesn't exist");
        return false;
    }

    public void sendHarvesterData(HarvesterResult harvesterResult, String md5) {

        StringBuilder query = new StringBuilder("INSERT INTO tcs__queue_list (file_md5checksum, departure_date, code_from, code_to,coach_class, train_brand_code, train_class_code, train_number, departure_time_offset_sec, duration_time_offset_sec, price_value, currency_code, fare_code, change_stations) VALUES ");

        for (TrainTicket ticket : harvesterResult.getTickets()) {

            String depTime = !ticket.getDepartureTime().equals("NULL") ? ticket.getDepartureTime() : "0";
            String duration = !ticket.getDuration().equals("NULL") ? ticket.getDuration() : "0";
            String price = !ticket.getPrice().equals("NULL") ? ticket.getPrice() : "0";

            query.append("(" +
                    "'" + md5 + "'" +
                    ",'" + ticket.getDate() + "'" +
                    ",'" + ticket.getDeparture() + "'" +
                    ",'" + ticket.getArrival() + "'" +
                    ",'" + ticket.getCoachClassName() + "'" +
                    ",'" + ticket.getTrainBrand() + "'" +
                    ",'" + ticket.getTrainClass() + "'" +
                    ",'" + ticket.getTrainNumber() + "'" +
                    ","  + depTime +
                    ","  + duration +
                    ","  + price +
                    ",'" + ticket.getCurrency() + "'" +
                    ",'" + ticket.getFare() + "'" +
                    ",'" + ticket.getChangeStation() + "'" +
                    "),");
        }

        String resultQuery = query.substring(0, query.toString().lastIndexOf(","));
        insertData(resultQuery);
        System.out.println("Completed");
    }

    public void insertData(String query) {
        try {
            Connection connection = dbConnection.getConnection();
            Statement statement = connection.createStatement();
            Class.forName(driver);
            log.info("send query: {}", query);
            statement.executeUpdate(query);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
