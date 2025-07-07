package services;

import model.Attendance;
import model.Event;
import model.Exam;
import model.Notification;
import model.NotificationType;
import model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import repository.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final AttendanceRepository attendanceRepo;
    private final NotificationRepository notificationRepo;
    private final StudentRepository studentRepo;
    private final SessionRepository sessionRepo;
    private final ExamRepository examRepo;
    private final EmailService emailService;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    public NotificationService(
            AttendanceRepository attendanceRepo,
            NotificationRepository notificationRepo,
            StudentRepository studentRepo,
            SessionRepository sessionRepo,
            ExamRepository examRepo,
            EmailService emailService) {

        this.attendanceRepo = attendanceRepo;
        this.notificationRepo = notificationRepo;
        this.studentRepo = studentRepo;
        this.sessionRepo = sessionRepo;
        this.examRepo = examRepo;
        this.emailService = emailService;
    }

    // DTO for user notification count
    public static class UserNotificationCount {
        private Long userId;
        private Long notificationCount;

        public UserNotificationCount(Long userId, Long notificationCount) {
            this.userId = userId;
            this.notificationCount = notificationCount;
        }

        public Long getUserId() {
            return userId;
        }

        public Long getNotificationCount() {
            return notificationCount;
        }
    }

    // DTO for user unread notification rate
    public static class UserUnreadRate {
        private Long userId;
        private double unreadRate;

        public UserUnreadRate(Long userId, double unreadRate) {
            this.userId = userId;
            this.unreadRate = unreadRate;
        }

        public Long getUserId() {
            return userId;
        }

        public double getUnreadRate() {
            return unreadRate;
        }
    }

    // Analytics method: top N most notified users
    public List<UserNotificationCount> getMostNotifiedUsers(int topN) {
        List<Student> allStudents = studentRepo.findAll();
        List<UserNotificationCount> counts = new ArrayList<>();

        for (Student student : allStudents) {
            Long userId = (long) student.getStudent_id();
            long totalNotifs = notificationRepo.countByTargetUserId(userId);
            counts.add(new UserNotificationCount(userId, totalNotifs));
        }

        return counts.stream()
                .sorted(Comparator.comparing(UserNotificationCount::getNotificationCount).reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }
    // Analytics method: unread notification rate per user
    public List<UserUnreadRate> getUnreadNotificationRates() {
        List<Student> allStudents = studentRepo.findAll();
        List<UserUnreadRate> rates = new ArrayList<>();

        for (Student student : allStudents) {
            Long userId = (long) student.getStudent_id();
            long total = notificationRepo.countByTargetUserId(userId);
            long unread = notificationRepo.countByTargetUserIdAndReadFalse(userId);
            double rate = total == 0 ? 0.0 : ((double) unread / total) * 100;
            rates.add(new UserUnreadRate(userId, rate));
        }

        return rates;
    }


    // --- your existing methods below (attendance checks, exam/event reminders, etc.) ---

    private double calculateAttendancePercentage(Student student) {
        long totalSessions = sessionRepo.count();
        if (totalSessions == 0) return 0.0;

        long attendedSessions = attendanceRepo.countByStudent(student);
        return (attendedSessions * 100.0) / totalSessions;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void checkAttendanceAndNotify() {
        List<Student> allStudents = studentRepo.findAll();

        for (Student student : allStudents) {
            double percentage = calculateAttendancePercentage(student);

            if (percentage < 75) {
                Notification notif = new Notification();
                notif.setTitle("Low Attendance Warning");
                notif.setBody("Your attendance is below 75%. Please improve.");
                notif.setType(NotificationType.ALERT);
                notif.setTargetUserId((long) student.getStudent_id());
                notificationRepo.save(notif);

                String email = student.getEmail();
                if (email != null && !email.isEmpty()) {
                    emailService.sendEmail(
                            email,
                            "Low Attendance Warning",
                            "Dear " + student.getFullName() +
                                    ",\n\nYour attendance is below 75%. Please improve."
                    );
                }
            }
        }
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void sendExamReminders() {
        LocalDate today = LocalDate.now();
        LocalDate soon = today.plusDays(3);

        List<Exam> upcomingExams = examRepo.findByExamDateBetween(today, soon);
        List<Student> allStudents = studentRepo.findAll();

        for (Exam exam : upcomingExams) {
            for (Student student : allStudents) {
                Notification notif = new Notification();
                notif.setTitle("📘 Upcoming Exam Reminder");
                notif.setBody("Reminder: " + exam.getSubject() + " exam is on " + exam.getExamDate());
                notif.setType(NotificationType.REMINDER_EXAM);
                notif.setTargetUserId((long) student.getStudent_id());
                notificationRepo.save(notif);

                String email = student.getEmail();
                if (email != null && !email.isEmpty()) {
                    emailService.sendEmail(
                            email,
                            "Upcoming Exam: " + exam.getSubject(),
                            "Dear " + student.getFullName() +
                                    ",\n\nYour " + exam.getSubject() + " exam is scheduled for " +
                                    exam.getExamDate() + ". Good luck!"
                    );
                }
            }
        }
    }

    @Scheduled(cron = "0 30 7 * * *")
    public void sendEventReminders() {
        LocalDate today = LocalDate.now();
        LocalDate soon = today.plusDays(2);

        List<Event> upcomingEvents = eventRepo.findByEventDateBetween(today, soon);
        List<Student> allStudents = studentRepo.findAll();

        for (Event event : upcomingEvents) {
            for (Student student : allStudents) {
                Notification notif = new Notification();
                notif.setTitle("📅 Upcoming Event Reminder");
                notif.setBody("Reminder: " + event.getTitle() + " on " + event.getEventDate() +
                        "\nDetails: " + event.getDescription());
                notif.setType(NotificationType.REMINDER_GENERAL);
                notif.setTargetUserId((long) student.getStudent_id());
                notificationRepo.save(notif);

                String email = student.getEmail();
                if (email != null && !email.isEmpty()) {
                    emailService.sendEmail(
                            email,
                            "Upcoming Event: " + event.getTitle(),
                            "Dear " + student.getFullName() +
                                    ",\n\nYou have an upcoming event: " + event.getTitle() +
                                    " on " + event.getEventDate() + ".\n\nDetails:\n" + event.getDescription()
                    );
                }
            }
        }
    }
    @Scheduled(cron = "0 0 2 * * *") // every day at 2 AM
    public void deleteOldNotifications() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        int deletedCount = notificationRepo.deleteByCreatedAtBefore(cutoffDate);
        System.out.println("Deleted " + deletedCount + " old notifications");
    }

    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepo.findByTargetUserIdOrTargetUserIdIsNull(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepo.findByTargetUserIdAndReadFalse(userId);
    }

    public Notification sendNotification(Notification notification) {
        return notificationRepo.save(notification);
    }

    public void markAsRead(Long id) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepo.save(notification);
    }
}
