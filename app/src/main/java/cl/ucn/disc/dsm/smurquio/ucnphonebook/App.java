package cl.ucn.disc.dsm.smurquio.ucnphonebook;

import android.app.Application;
import android.content.Context;

import org.acra.*;
import org.acra.annotation.*;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.DialogConfigurationBuilder;
import org.acra.config.MailSenderConfiguration;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;

/**
 * Main App.
 * @author Sebastian Murquio Castillo
 */
@AcraCore(buildConfigClass = BuildConfig.class)
public class App extends Application {

  /**
   * @param  base context to use.
   */
  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);

    //ACRA Configuration
    CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this);
    builder
        .withBuildConfigClass(BuildConfig.class)
        .withReportFormat(StringFormat.JSON)
        .withEnabled(true);

    //ACRA dialog configuration
    builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class)
        .withResText(R.string.acra_dialog_title)
        .withResCommentPrompt(R.string.acra_dialog_comment)
        .withEnabled(true);

    //ACRA email configuration
    builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder.class)
        .withMailTo("sebastian.murquio@alumnos.ucn.cl")
        .withReportFileName("crash.txt")
        .withSubject(getString(R.string.acra_dialog_title))
        .withBody(getString(R.string.acra_dialog_comment))
        .withEnabled(true);

    //The following line triggers the initialization of ACRA
    ACRA.init(this, builder);
  }
}
