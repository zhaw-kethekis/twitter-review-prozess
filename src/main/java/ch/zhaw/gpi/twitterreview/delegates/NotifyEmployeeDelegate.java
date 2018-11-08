/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.zhaw.gpi.twitterreview.delegates;

import ch.zhaw.gpi.twitterreview.services.EmailService;
import javax.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation des Send Task "Mitarbeiter benachrichtigen"
 *
 * @author Kisanth
 */
@Named("notifyEmployeeAdapter")
public class NotifyEmployeeDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    /**
     * 1. Die benötigten Prozessvariablen auslesen 2. Die E-Mail-Nachricht
     * zusammenstellen 3. Die E-Mail in der Konsole ausgebe
     *
     * @param de Objekt, welches die Verknüpfung zur Process Engine und zur
     * aktuellen Execution enthält
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution de) throws Exception {
        // Prozessvariablen auslesen
        String email = (String) de.getVariable("email");
        String tweetContent = (String) de.getVariable("tweetContent");
        String checkResult = (String) de.getVariable("checkResult");
        String checkResultComment = (String) de.getVariable("checkResultComment");
        String mailMainPart = (String) de.getVariable("mailMainPart");

        // Die E-Mail-Nachricht zusammenbauen
        String mailHauptteil;
        if (mailMainPart != null) {
            mailHauptteil = mailMainPart;
        } else if (checkResult.equals("rejected")) {
            mailHauptteil = "Leider wurde diese Tweet-Anfrage abgelehnt mit "
                    + "folgender Begründung:\n" + checkResultComment;
        } else {
            mailHauptteil = "Dein Tweet wurde geposted. Herzlichen Dank für Deinen Beitrag.";
        }

        // Mail-Text zusammenbauen
        String mailBody = "Hallo Mitarbeiter\n\n" + "Du hast folgenden Text zum "
                + "Veröffentlichen als Tweet vorgeschlagen:\n" + tweetContent + "\n\n"
                + mailHauptteil + "\n\n" + "Deine Kommunikationsabteilung";

        // Mail in Konsole ausgeben
        /*
        System.out.println("########### BEGIN MAIL ##########################");
        System.out.println("############################### Mail-Empfänger: " + email);
        System.out.println(mailBody);
        System.out.println("########### END MAIL ############################");
         */
        // Mail über Mailservice versenden
        emailService.sendSimpleMail(email, "Neuigkeiten zu Ihrer Tweet-Anfrage", mailBody);
    }

}
