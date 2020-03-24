package com.finalproject.frontend.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.finalproject.frontend.model.FileUploadRequest;
import com.finalproject.frontend.model.User;
import com.finalproject.frontend.repository.UserRepository;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static j2html.TagCreator.*;
import static j2html.attributes.Attr.ENCTYPE;
import static java.lang.String.format;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@RestController
public class FrontEndController {

  private final UserRepository userRepository;
  private final AmazonS3 amazonS3;

  public FrontEndController(final UserRepository userRepository, final AmazonS3 amazonS3) {
    this.userRepository = userRepository;
    this.amazonS3 = amazonS3;
  }

  @GetMapping
  public String showForm(HttpServletRequest request, HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_OK);
    return renderUploadForm();
  }

  @SneakyThrows
  @PostMapping(value = "/upload")
  public String uploadFile(@ModelAttribute FileUploadRequest fileUploadRequest, HttpServletResponse response) {
    createUser(fileUploadRequest.getUser());
    uploadFileToS3(fileUploadRequest);
    return renderUploadSuccess(fileUploadRequest.getUser());
  }

  private void createUser(User user) {
    userRepository.save(user);
  }

  @SneakyThrows
  private void uploadFileToS3(FileUploadRequest fileUploadRequest) {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.addUserMetadata("userId", fileUploadRequest.getUser().getUserId());
    metadata.setContentLength(fileUploadRequest.getFileUpload().getSize());
    metadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
    amazonS3.putObject(
        "untreated-files",
        format("%s/%s", UUID.randomUUID(), fileUploadRequest.getFileUpload().getOriginalFilename()), fileUploadRequest.getFileUpload().getInputStream(),
        metadata);
  }

  @SneakyThrows
  private String renderUploadSuccess(User user) {
    return html(
        head(getStyingTag()),
        body(
            h1(format("Thanks %s, Your file has been successfully uploaded", user.getFirstName())),
            h2(format("Once processed, an email will be sent to %s", user.getEmailAddress()))
        )
    ).renderFormatted();
  }

  @SneakyThrows
  private String renderUploadForm() {
    return html(
        head(getStyingTag()),
        body(
            form(
                h1("File Assurance Upload"),
                div(
                    label("First name").attr("for", "user.firstName"),
                    input().withType("text").withName("user.firstName").withId("user.firstName").withClass("form-control").isRequired()
                ).withClass("form-group"),
                div(
                    label("Last name").attr("for", "user.lastName"),
                    input().withType("text").withName("user.lastName").withId("user.lastName").withClass("form-control").isRequired()
                ).withClass("form-group"),
                div(
                    label("Email Address").attr("for", "user.emailAddress"),
                    input().withType("email").withName("user.emailAddress").withId("user.emailAddress").withClass("form-control").isRequired()
                ).withClass("form-group"),
                div(
                    label("File to process").attr("for", "fileUpload"),
                    input().withType("file").withName("fileUpload").withId("fileUpload").isRequired(),
                    div(
                        div("Great, your file is selected.").withClass("success"),
                        div("Please select a file to upload").withClass("default")
                    ).withClass("file-dummy")
                ).withClass("form-group file-area"),
                div(
                    button("Upload file").withType("submit")
                ).withClass("form-group")
            ).withAction("upload").withMethod("post").attr(ENCTYPE, MULTIPART_FORM_DATA),
            link().withHref("https://fonts.googleapis.com/css?family=Lato:100,200,300,400,500,600,700").withRel("stylesheet").withType("text/css")
        )
    ).renderFormatted();
  }

  @SneakyThrows
  private ContainerTag getStyingTag() {
    return style(IOUtils.toString(getClass().getResourceAsStream("/formCss.css"), StandardCharsets.UTF_8));
  }
}
