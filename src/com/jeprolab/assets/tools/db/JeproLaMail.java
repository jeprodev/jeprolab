package com.jeprolab.assets.tools.db;

import com.jeprolab.assets.config.JeproLabConfigurationSettings;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.models.JeproLabLaboratoryModel;
import com.jeprolab.models.JeproLabSettingModel;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by jeprodev on 7/23/16.
 */
public class JeproLaMail {
    public static boolean send(int langId, String template, String subject, Map<String, String> templateVars, String destMailAddress){
        return  send(langId, template, subject, templateVars, destMailAddress, "", "", "", null, null, JeproLabConfigurationSettings.JEPROLAB_MAILING_DIRECTORY, false, 0, null, "");
    }

    public static boolean send(int langId, String template, String subject, Map<String, String> templateVars, String destMailAddress, String toName){
        return send(langId, template, subject, templateVars, destMailAddress, toName, "", "", null, null, JeproLabConfigurationSettings.JEPROLAB_MAILING_DIRECTORY, false, 0, null, "");
    }

    public static boolean send(int langId, String template, String subject, Map<String, String> templateVars, String destMailAddress, String toName, String mailFrom){
        return send(langId, template, subject, templateVars, destMailAddress, toName, mailFrom, "", null, null, JeproLabConfigurationSettings.JEPROLAB_MAILING_DIRECTORY, false, 0, null, "");
    }

    public static boolean send(int langId, String template, String subject, Map<String, String> templateVars, String destMailAddress, String toName, String mailFrom, String fromName){
        return send(langId, template, subject, templateVars, destMailAddress, toName, mailFrom, fromName, null, null, JeproLabConfigurationSettings.JEPROLAB_MAILING_DIRECTORY, false, 0, null, "");
    }

    public static boolean send(int langId, String template, String subject, Map<String, String> templateVars, String destMailAddress, String toName, String mailFrom, String fromName, List attachedFile){
        return send(langId, template, subject, templateVars, destMailAddress, toName, mailFrom, fromName, attachedFile, null, JeproLabConfigurationSettings.JEPROLAB_MAILING_DIRECTORY, false, 0, null, "");
    }

    public static boolean send(int langId, String template, String subject, Map<String, String> templateVars, String destMailAddress, String toName, String mailFrom, String fromName, List attachedFile, String modeSmtp){
        return send(langId, template, subject, templateVars, destMailAddress, toName, mailFrom, fromName, attachedFile, modeSmtp, JeproLabConfigurationSettings.JEPROLAB_MAILING_DIRECTORY, false, 0, null, "");
    }

    public static boolean send(int langId, String template, String subject, Map<String, String> templateVars, String destMailAddress, String toName, String mailFrom, String fromName, List attachedFile, String modeSmtp, String templatePath, boolean die){
        return send(langId, template, subject, templateVars, destMailAddress, toName, mailFrom, fromName, attachedFile, modeSmtp, templatePath, die, 0, null, "");
    }

    public static boolean send(int langId, String template, String subject, Map<String, String> templateVars, String destMailAddress, String toName, String mailFrom, String fromName, List attachedFile, String modeSmtp, String templatePath, boolean die, int labId){
        return send(langId, template, subject, templateVars, destMailAddress, toName, mailFrom, fromName, attachedFile, modeSmtp, templatePath, die, labId, null, "");
    }

    public static boolean send(int langId, String template, String subject, Map<String, String> templateVars, String destMailAddress, String toName, String mailFrom, String fromName, List attachedFile, String modeSmtp, String templatePath, boolean die, int labId, List<String> copyTo){
        return send(langId, template, subject, templateVars, destMailAddress, toName, mailFrom, fromName, attachedFile, modeSmtp, templatePath, die, labId, copyTo, "");
    }

    public static boolean send(int langId, String template, String subject, Map<String, String> templateVars, String destMailAddress, String toName, String mailFrom, String fromName, List attachedFile, String modeSmtp, String templatePath, boolean die, int labId, List<String> copyTo, String replyTo){
        if(labId <= 0) {
            labId = JeproLabContext.getContext().laboratory.laboratory_id;
        }

        String labMail = JeproLabSettingModel.getStringValue("lab_mail");
        String labName = JeproLabSettingModel.getStringValue("lab_name");
        int mailMethod = JeproLabSettingModel.getIntValue("mail_method");
        String mailServer = JeproLabSettingModel.getStringValue("mail_server");
        String mailUser = JeproLabSettingModel.getStringValue("mail_user");
        String mailPassWord = JeproLabSettingModel.getStringValue("mail_password");
        String mailType = JeproLabSettingModel.getStringValue("mail_type");
        //String = JeproLabSettingModel.getValue("");
        //String = JeproLabSettingModel.getValue("");
        int smtpMailPort = JeproLabSettingModel.getIntValue("smtp_mail_port");
        String stmpMailEncryption = JeproLabSettingModel.getStringValue("mail_stmp_encryption");



        // Returns immediately if emails are deactivated
        if (mailMethod == 3) {
            return true;
        }

        /* String themePath = JeproLabConfigurationSettings.JEPROLAB_THEME_DIRECTORY;

        // Get the path of theme by laboratory id if exist
        if (labId > 0) {
            JeproLabLaboratoryModel lab = new JeproLabLaboratoryModel(labId);
            String themeName = lab.getThemeName();

            if (_THEME_NAME_ != $theme_name) {
                themePath = _PS_ROOT_DIR_ + "/themes/" + themeName + "/";
            }
        }

        if (!isset($configuration['PS_MAIL_SMTP_ENCRYPTION']) || Tools::strtolower($configuration['PS_MAIL_SMTP_ENCRYPTION']) === 'off') {
            $configuration['PS_MAIL_SMTP_ENCRYPTION'] = false;
        }
        if (!isset($configuration['PS_MAIL_SMTP_PORT'])) {
            $configuration['PS_MAIL_SMTP_PORT'] = 'default';
        }

        // Sending an e-mail can be of vital importance for the merchant, when his password is lost for example, so we must not die but do our best to send the e-mail

        if (!isset($from) || !Validate::isEmail($from)) {
            $from = $configuration['PS_SHOP_EMAIL'];
        }

        if (!Validate::isEmail($from)) {
            $from = null;
        }

        // $from_name is not that important, no need to die if it is not valid
        if (!isset($from_name) || !Validate::isMailName($from_name)) {
            $from_name = $configuration['PS_SHOP_NAME'];
        }
        if (!Validate::isMailName($from_name)) {
            $from_name = null;
        }

        // It would be difficult to send an e-mail if the e-mail is not valid, so this time we can die if there is a problem
        if (!is_array($to) && !Validate::isEmail($to)) {
            Tools::dieOrLog(Tools::displayError('Error: parameter "to" is corrupted'), $die);
            return false;
        }

        // if bcc is not null, make sure it's a vaild e-mail
        if (!is_null($bcc) && !is_array($bcc) && !Validate::isEmail($bcc)) {
            Tools::dieOrLog(Tools::displayError('Error: parameter "bcc" is corrupted'), $die);
            $bcc = null;
        }

        if (!is_array($template_vars)) {
            $template_vars = array();
        }

        // Do not crash for this error, that may be a complicated customer name
        if (is_string($to_name) && !empty($to_name) && !Validate::isMailName($to_name)) {
            $to_name = null;
        }

        if (!Validate::isTplName($template)) {
            Tools::dieOrLog(Tools::displayError('Error: invalid e-mail template'), $die);
            return false;
        }

        if (!Validate::isMailSubject($subject)) {
            Tools::dieOrLog(Tools::displayError('Error: invalid e-mail subject'), $die);
            return false;
        }

        /* Construct multiple recipients list if needed * /
        $message = Swift_Message::newInstance();
        if (is_array($to) && isset($to)) {
            foreach ($to as $key => $addr) {
                $addr = trim($addr);
                if (!Validate::isEmail($addr)) {
                    Tools::dieOrLog(Tools::displayError('Error: invalid e-mail address'), $die);
                    return false;
                }

                if (is_array($to_name) && $to_name && is_array($to_name) && Validate::isGenericName($to_name[$key])) {
                    $to_name = $to_name[$key];
                }

                $to_name = (($to_name == null || $to_name == $addr) ? '' : self::mimeEncode($to_name));
                $message->addTo($addr, $to_name);
            }
            $to_plugin = $to[0];
        } else {
            /* Simple recipient, one address * /
            $to_plugin = $to;
            $to_name = (($to_name == null || $to_name == $to) ? '' : self::mimeEncode($to_name));
            $message->addTo($to, $to_name);
        }
        if (isset($bcc)) {
            $message->addBcc($bcc);
        }

        try {
            /* Connect with the appropriate configuration * /
            if ($configuration['PS_MAIL_METHOD'] == 2) {
                if (empty($configuration['PS_MAIL_SERVER']) || empty($configuration['PS_MAIL_SMTP_PORT'])) {
                    Tools::dieOrLog(Tools::displayError('Error: invalid SMTP server or SMTP port'), $die);
                    return false;
                }

                $connection = Swift_SmtpTransport::newInstance($configuration['PS_MAIL_SERVER'], $configuration['PS_MAIL_SMTP_PORT'], $configuration['PS_MAIL_SMTP_ENCRYPTION'])
                        ->setUsername($configuration['PS_MAIL_USER'])
                ->setPassword($configuration['PS_MAIL_PASSWD']);

            } else {
                $connection = Swift_MailTransport::newInstance();
            }

            if (!$connection) {
                return false;
            }
            $swift = Swift_Mailer::newInstance($connection);
            /* Get templates content * /
            $iso = Language::getIsoById((int)$id_lang);
            if (!$iso) {
                Tools::dieOrLog(Tools::displayError('Error - No ISO code for email'), $die);
                return false;
            }
            $iso_template = $iso.'/'.$template;

            $module_name = false;
            $override_mail = false;

            // get templatePath
            if (preg_match('#'.$shop->physical_uri.'modules/#', str_replace(DIRECTORY_SEPARATOR, '/', $template_path)) && preg_match('#modules/([a-z0-9_-]+)/#ui', str_replace(DIRECTORY_SEPARATOR, '/', $template_path), $res)) {
                $module_name = $res[1];
            }

            if ($module_name !== false && (file_exists($theme_path.'modules/'.$module_name.'/mails/'.$iso_template.'.txt') ||
            file_exists($theme_path.'modules/'.$module_name.'/mails/'.$iso_template.'.html'))) {
                $template_path = $theme_path.'modules/'.$module_name.'/mails/';
            } elseif (file_exists($theme_path.'mails/'.$iso_template.'.txt') || file_exists($theme_path.'mails/'.$iso_template.'.html')) {
                $template_path = $theme_path.'mails/';
                $override_mail  = true;
            }
            if (!file_exists($template_path.$iso_template.'.txt') && ($configuration['PS_MAIL_TYPE'] == Mail::TYPE_BOTH || $configuration['PS_MAIL_TYPE'] == Mail::TYPE_TEXT)) {
                Tools::dieOrLog(Tools::displayError('Error - The following e-mail template is missing:').' '.$template_path.$iso_template.'.txt', $die);
                return false;
            } elseif (!file_exists($template_path.$iso_template.'.html') && ($configuration['PS_MAIL_TYPE'] == Mail::TYPE_BOTH || $configuration['PS_MAIL_TYPE'] == Mail::TYPE_HTML)) {
                Tools::dieOrLog(Tools::displayError('Error - The following e-mail template is missing:').' '.$template_path.$iso_template.'.html', $die);
                return false;
            }
            $template_html = '';
            $template_txt = '';
            Hook::exec('actionEmailAddBeforeContent', array(
                    'template' => $template,
                    'template_html' => &$template_html,
                    'template_txt' => &$template_txt,
                    'id_lang' => (int)$id_lang
            ), null, true);
            $template_html .= Tools::file_get_contents($template_path.$iso_template.'.html');
            $template_txt .= strip_tags(html_entity_decode(Tools::file_get_contents($template_path.$iso_template.'.txt'), null, 'utf-8'));
            Hook::exec('actionEmailAddAfterContent', array(
                    'template' => $template,
                    'template_html' => &$template_html,
                    'template_txt' => &$template_txt,
                    'id_lang' => (int)$id_lang
            ), null, true);
            if ($override_mail && file_exists($template_path.$iso.'/lang.php')) {
                include_once($template_path.$iso.'/lang.php');
            } elseif ($module_name && file_exists($theme_path.'mails/'.$iso.'/lang.php')) {
                include_once($theme_path.'mails/'.$iso.'/lang.php');
            } elseif (file_exists(_PS_MAIL_DIR_.$iso.'/lang.php')) {
                include_once(_PS_MAIL_DIR_.$iso.'/lang.php');
            } else {
                Tools::dieOrLog(Tools::displayError('Error - The language file is missing for:').' '.$iso, $die);
                return false;
            }

            /* Create mail and attach differents parts * /
            $subject = '['.Configuration::get('PS_SHOP_NAME', null, null, $id_shop).'] '.$subject;
            $message->setSubject($subject);

            $message->setCharset('utf-8');

            /* Set Message-ID - getmypid() is blocked on some hosting * /
            $message->setId(Mail::generateId());

            if (!($reply_to && Validate::isEmail($reply_to))) {
                $reply_to = $from;
            }

            if (isset($reply_to) && $reply_to) {
                $message->setReplyTo($reply_to);
            }

            $template_vars = array_map(array('Tools', 'htmlentitiesDecodeUTF8'), $template_vars);
            $template_vars = array_map(array('Tools', 'stripslashes'), $template_vars);

            if (Configuration::get('PS_LOGO_MAIL') !== false && file_exists(_PS_IMG_DIR_.Configuration::get('PS_LOGO_MAIL', null, null, $id_shop))) {
                $logo = _PS_IMG_DIR_.Configuration::get('PS_LOGO_MAIL', null, null, $id_shop);
            } else {
                if (file_exists(_PS_IMG_DIR_.Configuration::get('PS_LOGO', null, null, $id_shop))) {
                    $logo = _PS_IMG_DIR_.Configuration::get('PS_LOGO', null, null, $id_shop);
                } else {
                    $template_vars['{shop_logo}'] = '';
                }
            }
            JeproLabLaboratoryModel.JeproLabLaboratoryUrlModel.cacheMainDomainForLaboratory(labId);
            /* don't attach the logo as * /
            if (isset($logo)) {
                $template_vars['{shop_logo}'] = $message->embed(Swift_Image::fromPath($logo));
            }

            if ((Context::getContext()->link instanceof Link) === false) {
                Context::getContext()->link = new Link();
            }

            $template_vars['{shop_name}'] = Tools::safeOutput(Configuration::get('PS_SHOP_NAME', null, null, $id_shop));
            $template_vars['{shop_url}'] = Context::getContext()->link->getPageLink('index', true, Context::getContext()->language->id, null, false, $id_shop);
            $template_vars['{my_account_url}'] = Context::getContext()->link->getPageLink('my-account', true, Context::getContext()->language->id, null, false, $id_shop);
            $template_vars['{guest_tracking_url}'] = Context::getContext()->link->getPageLink('guest-tracking', true, Context::getContext()->language->id, null, false, $id_shop);
            $template_vars['{history_url}'] = Context::getContext()->link->getPageLink('history', true, Context::getContext()->language->id, null, false, $id_shop);
            $template_vars['{color}'] = Tools::safeOutput(Configuration::get('PS_MAIL_COLOR', null, null, $id_shop));
            // Get extra template_vars
            $extra_template_vars = array();
            Hook::exec('actionGetExtraMailTemplateVars', array(
                    'template' => $template,
                    'template_vars' => $template_vars,
                    'extra_template_vars' => &$extra_template_vars,
                    'id_lang' => (int)$id_lang
            ), null, true);
            $template_vars = array_merge($template_vars, $extra_template_vars);
            $swift->registerPlugin(new Swift_Plugins_DecoratorPlugin(array($to_plugin => $template_vars)));
            if ($configuration['PS_MAIL_TYPE'] == Mail::TYPE_BOTH || $configuration['PS_MAIL_TYPE'] == Mail::TYPE_TEXT) {
                $message->addPart($template_txt, 'text/plain', 'utf-8');
            }
            if ($configuration['PS_MAIL_TYPE'] == Mail::TYPE_BOTH || $configuration['PS_MAIL_TYPE'] == Mail::TYPE_HTML) {
                $message->addPart($template_html, 'text/html', 'utf-8');
            }
            if ($file_attachment && !empty($file_attachment)) {
                // Multiple attachments?
                if (!is_array(current($file_attachment))) {
                    $file_attachment = array($file_attachment);
                }

                foreach ($file_attachment as $attachment) {
                    if (isset($attachment['content']) && isset($attachment['name']) && isset($attachment['mime'])) {
                        $message->attach(Swift_Attachment::newInstance()->setFilename($attachment['name'])->setContentType($attachment['mime'])->setBody($attachment['content']));
                    }
                }
            }
            /* Send mail * /
            $message->setFrom(array($from => $from_name));
            $send = $swift->send($message);

            JeproLabLaboratoryModel.JeproLabLaboratoryUrlModel.resetMainDomainCache();

            if ($send && Configuration::get('PS_LOG_EMAILS')) {
                $mail = new Mail();
                $mail->template = Tools::substr($template, 0, 62);
                $mail->subject = Tools::substr($subject, 0, 254);
                $mail->id_lang = (int)$id_lang;
                $recipients_to = $message->getTo();
                $recipients_cc = $message->getCc();
                $recipients_bcc = $message->getBcc();
                if (!is_array($recipients_to)) {
                    $recipients_to = array();
                }
                if (!is_array($recipients_cc)) {
                    $recipients_cc = array();
                }
                if (!is_array($recipients_bcc)) {
                    $recipients_bcc = array();
                }
                foreach (array_merge($recipients_to, $recipients_cc, $recipients_bcc) as $email => $recipient_name) {
                    /** @var Swift_Address $recipient * /
                    $mail->id = null;
                    $mail->recipient = Tools::substr($email, 0, 126);
                    $mail->add();
                }
            }

            return $send;
        } catch (Swift_SwiftException $e) {
            PrestaShopLogger::addLog(
                    'Swift Error: '.$e->getMessage(),
                    3,
                    null,
                    'Swift_Message'
            );

            return false;
        } */
        return false;
    }
}